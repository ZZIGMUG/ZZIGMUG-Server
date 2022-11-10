package zzigmug.server.utils.exception

import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

class ResponseMessage(
    val status: Int,
    val message: String?,
    val data: Any? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {

    companion object {
        fun toResponseEntity(responseCode: ResponseCode): ResponseEntity<ResponseMessage> {
            return ResponseEntity
                .status(responseCode.httpStatus.value())
                .body(
                    ResponseMessage(
                        status = responseCode.httpStatus.value(),
                        message = responseCode.message
                    )
                )
        }

        fun toResponseEntity(responseCode: ResponseCode, data: Any): ResponseEntity<ResponseMessage> {
            return ResponseEntity
                .status(responseCode.httpStatus.value())
                .body(
                    ResponseMessage(
                        status = responseCode.httpStatus.value(),
                        message = responseCode.message,
                        data = data
                    )
                )
        }
    }
}