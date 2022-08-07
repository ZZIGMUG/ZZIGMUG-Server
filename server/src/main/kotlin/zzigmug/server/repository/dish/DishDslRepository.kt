package zzigmug.server.repository.dish

import zzigmug.server.entity.Dish
import zzigmug.server.entity.User
import java.time.Instant
import java.time.LocalDateTime

interface DishDslRepository {

    fun findByUserAndCreatedAtBetween(user: User, startedAt: LocalDateTime, endedAt: LocalDateTime): List<Dish>
}