package zzigmug.server.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseCode
import java.security.SignatureException
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
        val accessToken = resolveToken(request)

        if (StringUtils.hasText(accessToken)) {
            val authentication = accessToken?.let {
                try {
                    jwtTokenProvider.validateAccessToken(it)
                } catch (e: SignatureException) {
                    sendErrorResponse(response, ResponseCode.TOKEN_INVALID_SIGNATURE)
                    return
                } catch (e: MalformedJwtException) {
                    sendErrorResponse(response, ResponseCode.TOKEN_INVALID)
                    return
                } catch (e: ExpiredJwtException) {
                    sendErrorResponse(response, ResponseCode.TOKEN_EXPIRED)
                    return
                } catch (e: UnsupportedJwtException) {
                    sendErrorResponse(response, ResponseCode.TOKEN_UNSUPPORTED)
                    return
                } catch (e: java.lang.IllegalArgumentException) {
                    sendErrorResponse(response, ResponseCode.TOKEN_EMPTY)
                    return
                }
                jwtTokenProvider.getAuthentication(it)
            }
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX))
            return bearerToken.substring(7)
        return null
    }

    private fun sendErrorResponse(response: HttpServletResponse, responseCode: ResponseCode) {
        val objectMapper = ObjectMapper()
        response.contentType = "application/json"

        val jsonString = objectMapper.writeValueAsString(ErrorResponse.toResponseEntity(responseCode))
        response.writer.print(jsonString)
        response.writer.flush()
    }
}