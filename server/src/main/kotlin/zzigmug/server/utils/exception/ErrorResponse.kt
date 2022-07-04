package zzigmug.server.utils.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val code: String,
    val message: String,
) {
    companion object {
        fun toResponseEntity(responseCode: ResponseCode): ResponseEntity<ErrorResponse> {
            return ResponseEntity
                .status(responseCode.httpStatus.value())
                .body(
                    ErrorResponse(
                        status = responseCode.httpStatus.value(),
                        error = responseCode.httpStatus.name,
                        code = responseCode.name,
                        message = responseCode.message
                    )
                )
        }
    }
}