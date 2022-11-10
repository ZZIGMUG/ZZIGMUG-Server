package zzigmug.server.utils.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(e: CustomException): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(e.responseCode)
    }
}