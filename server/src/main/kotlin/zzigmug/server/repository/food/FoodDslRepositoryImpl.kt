package zzigmug.server.repository.food

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import zzigmug.server.entity.Food
import zzigmug.server.entity.QFood
import zzigmug.server.repository.QuerydslCustomRepositorySupport

class FoodDslRepositoryImpl: QuerydslCustomRepositorySupport(Food::class.java), FoodDslRepository {
    private val food: QFood = QFood("food")

    override fun findAllFoodBySearch(pageable: Pageable, keyword: String?): Page<Food> {
        val foods = selectFrom(food)
            .where(nameLike(keyword))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(food.id.desc())
            .fetch()

        val countQuery = selectFrom(food)
            .where(nameLike(keyword))

        return PageableExecutionUtils.getPage(foods, pageable, countQuery::fetchCount)
    }

    private fun nameLike(keyword: String?): BooleanExpression? {
        return if (!keyword.isNullOrEmpty()) {
            food.name.like("%$keyword%")
        }
        else null
    }
}