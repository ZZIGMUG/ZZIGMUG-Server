package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.AuthService

@RestController
class AuthController(
    private val authService: AuthService
) {
}