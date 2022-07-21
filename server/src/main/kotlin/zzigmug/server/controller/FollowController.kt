package zzigmug.server.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.FollowService
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/follow")
class FollowController(
    private val followService: FollowService
) {
    @PostMapping
    fun followUser(@RequestParam followingId: Long, request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(followService.followUser(followingId, userEmail))
    }

    @DeleteMapping
    fun unfollowUser(@RequestParam followingId: Long, request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(followService.unfollowUser(followingId, userEmail))
    }

    @GetMapping("/follower")
    fun readAllFollower(@RequestParam params: MutableMap<String, String>): ResponseEntity<Any> {
        val userId = params["userId"]?.toLong() ?: throw CustomException(ResponseCode.BAD_REQUEST)
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(followService.readAllFollower(PageRequest.of(page, size), userId))
    }

    @GetMapping("/following")
    fun readAllFollowing(@RequestParam params: MutableMap<String, String>): ResponseEntity<Any> {
        val userId = params["userId"]?.toLong() ?: throw CustomException(ResponseCode.BAD_REQUEST)
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(followService.readAllFollowing(PageRequest.of(page, size), userId))
    }
}