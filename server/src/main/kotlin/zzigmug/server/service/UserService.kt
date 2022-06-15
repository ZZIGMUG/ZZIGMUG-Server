package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {
}