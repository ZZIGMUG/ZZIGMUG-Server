package zzigmug.server.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import zzigmug.server.utils.exception.ResponseCode
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint: AuthenticationEntryPoint {

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        val objectMapper = ObjectMapper()
        response.contentType = "application/json"
        val jsonString = objectMapper.writeValueAsString(ResponseCode.TOKEN_INVALID)
        response.writer.print(jsonString)
        response.writer.flush()
    }
}