package zzigmug.server.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.UserResponseDto
import zzigmug.server.data.UserPageResponseDto
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class UserService(
    private val authService: AuthService,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    fun readUserById(id: Long): UserResponseDto {
        val user = userRepository.findById(id).orElseThrow {
            throw CustomException(ResponseCode.USER_NOT_FOUND)
        }
        return UserResponseDto(user)
    }

    @Transactional(readOnly = true)
    fun readAll(pageable: Pageable, keyword: String?): UserPageResponseDto {
        val pages = userRepository.findAllUserBySearch(pageable, keyword)

        val userInfoList = mutableListOf<UserResponseDto>()
        pages.forEach {
            userInfoList.add(UserResponseDto(it))
        }
        return UserPageResponseDto(pages.totalElements, pages.totalPages, userInfoList)
    }

    @Transactional
    fun editNickname(userEmail: String, nickname: String): UserResponseDto {
        authService.validateNickname(nickname)

        val user = userRepository.findByEmail(userEmail)
            ?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        user.setNickname(nickname)

        return UserResponseDto(userRepository.save(user))
    }
}