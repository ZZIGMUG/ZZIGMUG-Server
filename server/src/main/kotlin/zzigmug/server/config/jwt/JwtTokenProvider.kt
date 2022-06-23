package zzigmug.server.config.jwt

import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import zzigmug.server.data.property.JwtProperty
import java.time.Duration
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val jwtProperty: JwtProperty,
    private val userDetailsService: UserDetailsService
) {
    // TODO: private val 보다 더 좋은 게 있는지 찾아보기
    private val ONE_DAY: Long = 1000 * 60 * 60 * 24
    private val ONE_WEEK: Long = ONE_DAY * 7

    private var secret: String = ""
    private var refresh: String = ""
    private val LOG: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    @PostConstruct
    fun init() {
        secret = Base64.getEncoder().encodeToString(jwtProperty.secret.toByteArray())
        refresh = Base64.getEncoder().encodeToString(jwtProperty.refresh.toByteArray())
    }

    fun getAccessToken(username: String, roles: Array<String>): String {
        return generate(username, ONE_DAY * 30, roles, secret)
    }

    fun getRefreshToken(username: String, roles: Array<String>): String {
        return generate(username, ONE_DAY * 180, roles, refresh)
    }

    private fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String {
        val now = Date()
        val claims = Jwts.claims().setSubject(username)
        claims.put("roles", roles)

        return Jwts.builder()
            .setIssuer("zzigmug")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + expirationInMillis))
            .signWith(SignatureAlgorithm.HS256, signature)
            .compact()
    }

    private fun getExpiration(expirationInMillis: Long): Long = Date().toInstant()
        .plus(Duration.ofMillis(expirationInMillis))
        .toEpochMilli()

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
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
}