package zzigmug.server.repository.dish

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import zzigmug.server.entity.Dish
import zzigmug.server.entity.Photo
import zzigmug.server.entity.User
import java.time.Instant
import java.time.LocalDateTime

@Repository
interface DishRepository : JpaRepository<Dish, Long>, DishDslRepository {}