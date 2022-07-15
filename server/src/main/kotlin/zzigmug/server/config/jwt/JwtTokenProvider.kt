package zzigmug.server.config.jwt

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import zzigmug.server.data.property.JwtProperty
import java.util.*
import javax.annotation.PostConstruct

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
    private val userDetailsService: UserDetailsService
) {
    private var secretKey = ""
    private var refreshKey = ""

    private val ONE_DAY: Long = 1000 * 60 * 60 * 24
    private val ONE_WEEK: Long = ONE_DAY * 7
    private val BEARER_PREFIX = "Bearer "
    private val LOG = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperty.secret.toByteArray())
        refreshKey = Base64.getEncoder().encodeToString(jwtProperty.refresh.toByteArray())
    }

    fun getAccessToken(username: String, roles: Array<String>): String {
        return BEARER_PREFIX + generate(username, ONE_DAY * 180, roles, secretKey)
    }

    fun validateAccessToken(accessToken: String?): Boolean {
        return validate(secretKey, accessToken)
    }

    fun getRefreshToken(username: String, roles: Array<String>): String {
        return BEARER_PREFIX + generate(username, ONE_DAY * 180, roles, refreshKey)
    }

    fun validateRefreshToken(refreshToken: String?): Boolean {
        return validate(refreshKey, refreshToken)
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
            LOG.error("Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            LOG.error("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            LOG.error("Expired JWT token")
        } catch (e: UnsupportedJwtException) {
            LOG.error("Unsupported JWT token")
        } catch (e: IllegalArgumentException) {
            LOG.error("JWT claims string is empty")
        }
        return false
    }

    fun getAuthentication(accessToken: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getEmail(accessToken))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getEmail(accessToken: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).body.subject
    }
}