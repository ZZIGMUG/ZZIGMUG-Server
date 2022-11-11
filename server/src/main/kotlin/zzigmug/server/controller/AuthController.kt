package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import zzigmug.server.data.*
import zzigmug.server.service.AuthService
import zzigmug.server.utils.exception.ResponseMessage
import zzigmug.server.utils.exception.ResponseCode

@Tag(name = "auth", description = "로그인/회원가입 API")
@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "이메일 회원가입", description = "이메일과 패스워드 및 회원 개인 정보를 입력받아 회원가입을 진행합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 & 로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 이름을 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))])])
    @PostMapping("/join/email")
    fun emailJoin(@Parameter(description = "가입할 유저 정보") @RequestBody requestDto: EmailJoinRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            authService.emailJoin(requestDto)
        )
    }

    @Operation(summary = "이메일 로그인", description = "이메일과 패스워드로 로그인합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호를 틀렸습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))])])
    @PostMapping("/login/email")
    fun emailLogin(@Parameter(description = "로그인할 유저 정보") @RequestBody requestDto: LoginRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            authService.emailLogin(requestDto)
        )
    }

    @Operation(summary = "카카오 액세스 토큰 API", description = "카카오 OAUTH 사이트로부터 액세스 토큰을 받는 API입니다. (서버 테스트용) ")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입/로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = String::class))))]),
    ])
    @GetMapping("/login/kakao/callback")
    fun getKakaoToken(code: String): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            authService.getKakaoToken(code)
        )
    }

    @Operation(summary = "카카오 로그인/회원가입 API", description = "카카오 액세스 토큰을 통해 로그인을 진행합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입/로그인 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
    ])
    @PostMapping("/join/kakao/token")
    fun kakaoLogin(@RequestBody requestDto: KakaoLoginRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            authService.kakaoLogin(requestDto.accessToken)
        )
    }

    @Operation(summary = "카카오 회원의 회원가입 API", description = "카카오 회원가입 유저로부터 회원가입 단계에서 입력받은 회원정보로 최종 회원가입을 진행합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 이메일의 회원을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]
        ),
    ])
    @PostMapping("/join/kakao")
    fun join(@RequestBody requestDto: JoinRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            authService.kakaoJoin(requestDto)
        )
    }

    @Operation(summary = "닉네임 중복확인", description = "닉네임 중복 여부를 확인합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "사용 가능한 닉네임입니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "409", description = "이미 같은 닉네임을 사용하는 유저가 존재합니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "401", description = "닉네임 형식이 올바르지 않습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))])
    ])
    @GetMapping("/check/nickname")
    fun checkNickname(@Parameter(description = "닉네임") @RequestParam nickname: String): ResponseEntity<ResponseMessage> {
        val responseCode = authService.validateNickname(nickname)

        return ResponseMessage.toResponseEntity(ResponseCode.NICKNAME_AVALIABLE)
    }
}