package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.JoinRequestDto
import zzigmug.server.data.LoginResponseDto
import zzigmug.server.service.AuthService
import zzigmug.server.utils.exception.ErrorResponse
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
    fun kakaoLogin(code: String): ResponseEntity<ResponseMessage> {
        return ResponseEntity
            .ok()
            .body(ResponseMessage("ok", authService.kakaoLogin(code)))
    }

    @Operation(summary = "회원가입 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원가입 성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = LoginResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 이메일의 회원을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]
        ),
    ])
    @PostMapping("/join")
    fun join(@RequestBody requestDto: JoinRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(ResponseMessage("ok", authService.join(requestDto)))
    }
}