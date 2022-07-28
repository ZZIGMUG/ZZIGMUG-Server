package zzigmug.server.repository.follow

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zzigmug.server.entity.Follow
import zzigmug.server.entity.User

@Repository
interface FollowRepository: JpaRepository<Follow, Long>, FollowDslRepository {
    fun findByFollowerAndFollowing(follower: User, following: User): Follow?

    fun existsByFollowerAndFollowing(follower: User, following: User): Boolean
}