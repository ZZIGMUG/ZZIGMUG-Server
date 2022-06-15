package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.FollowService

@RestController
class FollowController(
    private val followService: FollowService
) {
}