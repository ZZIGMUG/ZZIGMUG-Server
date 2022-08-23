package zzigmug.server.utils.exception

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val httpStatus: HttpStatus,
    val message: String,
) {
    OK(HttpStatus.OK, "성공"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "계정이 존재하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일이 이미 존재합니다."),
    NICKNAME_INCORRECT(HttpStatus.BAD_REQUEST, "닉네임 형식이 올바르지 않습니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "중복된 닉네임이 이미 존재합니다."),

    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 음식이 존재하지 않습니다."),
    FOOD_DUPLICATED(HttpStatus.CONFLICT, "중복된 음식이 이미 존재합니다."),

    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 사진이 존재하지 않습니다."),
    DISH_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 요리가 존재하지 않습니다."),

    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 상대방을 팔로우하고 있지 않습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 상대방을 팔로잉하고 있습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),

    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "이메일 또는 패스워드가 틀렸습니다."),

    TOKEN_INVALID_SIGNATURE(HttpStatus.NOT_FOUND, "Invalid JWT signature"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Expired JWT token"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "JWT claims string is empty"),
}

data class ResponseMessage (
    val status: Int,
    val data: Any
)