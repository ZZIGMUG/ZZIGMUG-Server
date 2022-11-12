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
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = jwtTokenProvider.resolveToken(request)

        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateAccessToken(accessToken)) {
            SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(accessToken!!)
        }
        filterChain.doFilter(request, response)
    }
}