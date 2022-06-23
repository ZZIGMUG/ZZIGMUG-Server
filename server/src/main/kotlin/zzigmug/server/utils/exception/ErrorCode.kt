package zzigmug.server.utils.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String,
) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "계정이 존재하지 않습니다."),
    USER_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 틀렸습니다."),
    USER_GITHUB_DUPLICATED(HttpStatus.CONFLICT, "이미 같은 깃헙 아이디를 사용하는 유저가 존재합니다."),
    USER_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 같은 이름을 사용하는 유저가 존재합니다."),

    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "액세스 토큰이 만료되었습니다."),
    TOKEN_NOT_EXPIRED(HttpStatus.BAD_REQUEST, "아직 만료되지 않은 토큰입니다."),
    ACCEPT_DENIED(HttpStatus.NOT_ACCEPTABLE, "접근이 거부되었습니다."),

    NOT_SUPPORTED_TYPE(HttpStatus.CONFLICT, "지원하지 않는 로그인 형식입니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "객체가 존재하지 않습니다."),

    COMMIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "커밋을 불러오는 데에 오류가 발생했습니다."),
    EVENT_EXPIRED(HttpStatus.BAD_REQUEST, "이미 지나간 이벤트입니다."),
    EVENT_NOT_STARTED(HttpStatus.BAD_REQUEST, "예정된 이벤트입니다."),
}