package zzigmug.server.utils.exception

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val httpStatus: HttpStatus,
    val message: String,
) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "계정이 존재하지 않습니다."),

    TOKEN_INVALID_SIGNATURE(HttpStatus.NOT_FOUND, "Invalid JWT signature"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Expired JWT token"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "JWT claims string is empty"),
}

data class ResponseMessage (
    val message: String,
    val data: Any
)