package zzigmug.server.config.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import zzigmug.server.data.property.JwtProperty
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
    private val jwtProperty: JwtProperty,
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    private lateinit var bearerPrefix: String
    private lateinit var authorizationHeader: String

    @PostConstruct
    fun init() {
        bearerPrefix = Base64.getEncoder().encodeToString(jwtProperty.prefix.toByteArray())
        authorizationHeader = Base64.getEncoder().encodeToString(jwtProperty.header.toByteArray())
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = resolveToken(request)

        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateAccessToken(accessToken)) {
            SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(accessToken!!)
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(authorizationHeader)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(bearerPrefix)) {
            return bearerToken.substring(7)
        }

        return null
    }
}