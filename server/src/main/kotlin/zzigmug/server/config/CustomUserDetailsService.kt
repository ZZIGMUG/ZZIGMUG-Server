package zzigmug.server.config

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import zzigmug.server.repository.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ErrorCode

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username).orElseThrow { throw CustomException(ErrorCode.USER_NOT_FOUND) }
    }
}