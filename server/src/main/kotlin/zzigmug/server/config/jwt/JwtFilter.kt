package zzigmug.server.config.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import zzigmug.server.config.SecurityConfig
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    private val AUTHORIZATION_HEADER = "Authorization"
    private val BEARER_PREFIX = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val url = request.requestURL.toString()
        url.lowercase(Locale.getDefault())
        val localApiPath = url.replace("http://localhost:8080", "")
        // TODO: 서버 배포 시 빈칸 채우기
        val devApiPath = url.replace("","")

        val excludedUrl = SecurityConfig.EXCLUDED_PATHS
        val isExcludedLocalUrl = excludedUrl.contains(localApiPath)
        val isExcludedDevPath = excludedUrl.contains(devApiPath)

        if (isExcludedLocalUrl || isExcludedDevPath) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = resolveToken(request)

        if (StringUtils.hasText(jwt)) {
            val authentication = jwtTokenProvider.getAuthentication(jwt!!)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX))
            return bearerToken.substring(7)

        return null
    }

}