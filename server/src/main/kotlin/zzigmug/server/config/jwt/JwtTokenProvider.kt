package zzigmug.server.config.jwt

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import zzigmug.server.data.property.JwtProperty
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
    private val userDetailsService: UserDetailsService
) {

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    companion object {
        private const val ONE_DAY = 1000L * 60 * 60 * 24
        private const val ONE_WEEK = ONE_DAY * 7
    }

    fun getAccessToken(username: String, roles: Array<String>): String {
        return jwtProperty.bearerPrefix + generate(username, ONE_DAY * 30, roles, jwtProperty.secretKey)
    }

    fun getRefreshToken(username: String, roles: Array<String>): String {
        return jwtProperty.bearerPrefix + generate(username, ONE_WEEK * 8, roles, jwtProperty.refreshKey)
    }

    fun validateAccessToken(accessToken: String?): Boolean {
        return validate(jwtProperty.secretKey, accessToken)
    }

    fun validateRefreshToken(refreshToken: String?): Boolean {
        return validate(jwtProperty.refreshKey, refreshToken)
    }

    private fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String {
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles

        val now = Date()
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expirationInMillis))
            .signWith(SignatureAlgorithm.HS256, signature)
            .compact()
    }

    fun validate(signature: String, token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(signature).parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            log.error("Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            log.error("Expired JWT token")
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported JWT token")
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty")
        }
        return false
    }

    fun getAuthentication(accessToken: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(extractEmailFromToken(accessToken))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun extractEmailFromToken(accessToken: String): String {
        return Jwts.parser().setSigningKey(jwtProperty.secretKey).parseClaimsJws(accessToken).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(jwtProperty.authorizationHeader)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperty.bearerPrefix)) {
            return bearerToken.substring(7)
        }
        return null
    }
}