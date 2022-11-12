package zzigmug.server.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import zzigmug.server.data.UserFollowingDto
import zzigmug.server.entity.Follow
import zzigmug.server.repository.follow.FollowRepository
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
) {

    fun followUser(followingId: Long, userEmail: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val followingUser = userRepository.findById(followingId).orElseThrow {
            throw CustomException(ResponseCode.USER_NOT_FOUND)
        }

        if (followRepository.existsByFollowerAndFollowing(user, followingUser)) {
            throw CustomException(ResponseCode.FOLLOW_DUPLICATED)
        }

        if (user.id == followingUser.id) {
            throw CustomException(ResponseCode.FOLLOW_BAD_REQUEST)
        }

        followRepository.save(Follow(user, followingUser))
    }

    fun unfollowUser(followingId: Long, userEmail: String) {
        val follower = userRepository.findByEmail(userEmail)
            ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val following = userRepository.findById(followingId).orElseThrow {
            throw CustomException(ResponseCode.USER_NOT_FOUND)
        }

        val follow = followRepository.findByFollowerAndFollowing(follower, following)
            ?: throw CustomException(ResponseCode.FOLLOW_NOT_FOUND)

        followRepository.delete(follow)
    }

    fun readAllFollower(pageable: Pageable, email: String): MutableList<UserFollowingDto> {
        val user = userRepository.findByEmail(email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val response = mutableListOf<UserFollowingDto>()
        followRepository.findAllFollower(pageable, user).forEach {
            response.add(UserFollowingDto(it))
        }
        return response
    }

    fun readAllFollowing(pageable: Pageable, email: String): MutableList<UserFollowingDto> {
        val user = userRepository.findByEmail(email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val response = mutableListOf<UserFollowingDto>()
        followRepository.findAllFollowing(pageable, user).forEach {
            response.add(UserFollowingDto(it))
        }
        return response
    }
}