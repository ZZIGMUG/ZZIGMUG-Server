package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.UserService

@RestController
class UserController(
    private val userService: UserService,
) {
}