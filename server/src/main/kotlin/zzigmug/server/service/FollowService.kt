package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.repository.FollowRepository

@Service
class FollowService(
    private val followRepository: FollowRepository,
) {
}