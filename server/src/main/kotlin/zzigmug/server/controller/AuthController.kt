package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import zzigmug.server.data.EmailJoinRequestDto
import zzigmug.server.data.JoinRequestDto
import zzigmug.server.data.LoginRequestDto
import zzigmug.server.data.LoginResponseDto
import zzigmug.server.service.AuthService
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage

@Tag(name = "auth", description = "로그인/회원가입 API")
@RestController
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "카카오 로그인 콜백 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입/로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
    ])
    @GetMapping("/login/kakao/callback")
    fun kakaoLogin(code: String): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(authService.kakaoLogin(code))
    }

    @Operation(summary = "이메일 회원가입")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 & 로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 이름을 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/join/email")
    fun emailJoin(@Parameter(description = "가입할 유저 정보") @RequestBody requestDto: EmailJoinRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(authService.emailJoin(requestDto))
    }

    @Operation(summary = "이메일 로그인")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호를 틀렸습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])])
    @PostMapping("/login/email")
    fun emailLogin(@Parameter(description = "로그인할 유저 정보") @RequestBody requestDto: LoginRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(authService.emailLogin(requestDto))
    }

    @Operation(summary = "카카오 회원의 회원가입 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 이메일의 회원을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]
        ),
    ])
    @PostMapping("/join/kakao")
    fun join(@RequestBody requestDto: JoinRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(authService.join(requestDto))
    }

    @Operation(summary = "닉네임 중복확인")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "사용 가능한 닉네임입니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 닉네임을 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
        ApiResponse(responseCode = "401", description = "닉네임 형식이 올바르지 않습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))])
    ])
    @GetMapping("/check/nickname")
    fun checkNickname(@Parameter(description = "닉네임") @RequestParam nickname: String): ResponseEntity<Any> {
        val responseCode = authService.validateNickname(nickname)

        if (responseCode == ResponseCode.OK)
            return ResponseEntity
                .ok()
                .body(
                    ResponseMessage(
                        status = responseCode.httpStatus.value(),
                        data = "사용 가능한 닉네임입니다."
                    )
                )

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