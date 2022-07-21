package zzigmug.server.repository.follow

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import zzigmug.server.entity.User

interface FollowDslRepository {

    fun findAllFollower(pageable: Pageable, follower: User): Page<User>

    fun findAllFollowing(pageable: Pageable, following: User): Page<User>
}