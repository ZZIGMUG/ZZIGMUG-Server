package zzigmug.server.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.UserInfo
import zzigmug.server.data.UserPage
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class UserService(
    private val authService: AuthService,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun readUserById(id: Long): UserInfo {
        val user = userRepository.findById(id).orElseThrow {
            throw CustomException(ResponseCode.USER_NOT_FOUND)
        }

        return UserInfo(user)
    }

    @Transactional(readOnly = true)
    fun readAll(pageable: Pageable, queryParams: MutableMap<String, String>): UserPage {
        val userList = mutableListOf<UserInfo>()
        val pages = userRepository.findAllUserBySearch(pageable, queryParams)

        pages.forEach {
            userList.add(UserInfo(it))
        }

        return UserPage(pages.totalElements, pages.totalPages, userList)
    }

    fun editNickname(email: String, nickname: String): UserInfo {
        if (authService.validateNickname(nickname) != ResponseCode.OK)
            throw CustomException(ResponseCode.NICKNAME_INCORRECT)

        val user = userRepository.findByEmail(email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        user.nickname = nickname
        userRepository.save(user)

        return UserInfo(user)
    }
}