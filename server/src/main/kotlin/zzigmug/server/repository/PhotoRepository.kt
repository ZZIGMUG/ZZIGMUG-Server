package zzigmug.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zzigmug.server.entity.Photo
import zzigmug.server.entity.User
import java.time.LocalDateTime

@Repository
interface PhotoRepository: JpaRepository<Photo, Long> {
    fun findByUserAndDateBetween(user: User, startedAt: LocalDateTime, endedAt: LocalDateTime): List<Photo>
}