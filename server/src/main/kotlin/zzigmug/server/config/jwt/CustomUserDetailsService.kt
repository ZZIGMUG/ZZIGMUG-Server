package zzigmug.server.config.jwt

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)
    }
}