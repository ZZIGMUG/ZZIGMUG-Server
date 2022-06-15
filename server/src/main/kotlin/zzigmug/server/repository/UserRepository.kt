package zzigmug.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zzigmug.server.entity.User

@Repository
interface UserRepository: JpaRepository<User, Long> {
}