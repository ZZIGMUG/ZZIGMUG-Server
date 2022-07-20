package zzigmug.server.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.UserService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{userId}")
    fun readUserInfo(@PathVariable userId: Long): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(userService.readUserById(userId))
    }

    @GetMapping("/list")
    fun searchUser(@RequestParam params: MutableMap<String, String>): ResponseEntity<Any> {
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(userService.readAll(PageRequest.of(page, size), params))
    }

    @PatchMapping("/nickname")
    fun editNickname(
        @RequestParam nickname: String,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val email = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(userService.editNickname(email, nickname))
    }
}