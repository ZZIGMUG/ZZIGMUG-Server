package zzigmug.server.repository.follow

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import zzigmug.server.entity.Follow
import zzigmug.server.entity.QFollow
import zzigmug.server.entity.User
import zzigmug.server.repository.QuerydslCustomRepositorySupport

@Repository
class FollowDslRepositoryImpl: QuerydslCustomRepositorySupport(Follow::class.java), FollowDslRepository {
    val follow = QFollow("follow")

    override fun findAllFollower(pageable: Pageable, user: User): Page<User> {
        val followers = select(follow.follower)
            .from(follow)
            .where(follow.following.eq(user))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(follow.id.desc())
            .fetch()

        val countQuery = select(follow.follower)
            .from(follow)
            .where(follow.following.eq(user))

        return PageableExecutionUtils.getPage(followers, pageable, countQuery::fetchCount)
    }

    override fun findAllFollowing(pageable: Pageable, user: User): Page<User> {
        val followings = select(follow.following)
            .from(follow)
            .where(follow.follower.eq(user))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(follow.id.desc())
            .fetch()

        val countQuery = select(follow.follower)
            .from(follow)
            .where(follow.following.eq(user))

        return PageableExecutionUtils.getPage(followings, pageable, countQuery::fetchCount)
    }
}