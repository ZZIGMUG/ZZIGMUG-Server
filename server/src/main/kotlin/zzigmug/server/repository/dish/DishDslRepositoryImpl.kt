package zzigmug.server.repository.dish

import com.querydsl.core.types.dsl.BooleanExpression
import zzigmug.server.entity.Dish
import zzigmug.server.entity.QDish
import zzigmug.server.entity.QPhoto
import zzigmug.server.entity.User
import zzigmug.server.repository.QuerydslCustomRepositorySupport
import java.time.Instant
import java.time.LocalDateTime

class DishDslRepositoryImpl: DishDslRepository, QuerydslCustomRepositorySupport(Dish::class.java) {
    private val photo = QPhoto("photo")
    private val dish = QDish("dish")

    override fun findByUserAndCreatedAtBetween(user: User, startedAt: LocalDateTime, endedAt: LocalDateTime): List<Dish> {
        return selectFrom(dish)
            .leftJoin(photo).on(dish.photo.id.eq(photo.id))
            .where(
                userLike(user),
                createdAtBetween(startedAt, endedAt),
            )
            .orderBy(dish.id.desc())
            .fetch()
    }

    private fun userLike(user: User): BooleanExpression {
        return photo.user.id.eq(user.id)
    }

    private fun createdAtBetween(startedAt: LocalDateTime, endedAt: LocalDateTime): BooleanExpression {
        return dish.createAt.between(startedAt, endedAt)
    }
}