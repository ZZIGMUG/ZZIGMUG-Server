package zzigmug.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zzigmug.server.entity.User
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun existsByNickname(nickname: String): Boolean
}