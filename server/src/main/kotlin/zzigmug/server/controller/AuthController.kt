package zzigmug.server.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.AuthService
import zzigmug.server.utils.response.ResponseMessage

@RestController
class AuthController(
    private val authService: AuthService
) {

    @GetMapping("/login/kakao")
    fun kakaoLogin(code: String): ResponseEntity<ResponseMessage> {
        return ResponseEntity.ok(ResponseMessage("ok", authService.kakaoLogin(code)))
    }
}