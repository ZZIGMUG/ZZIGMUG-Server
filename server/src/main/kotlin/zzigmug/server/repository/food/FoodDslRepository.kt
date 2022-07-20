package zzigmug.server.repository.food

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import zzigmug.server.entity.Food
import zzigmug.server.entity.User

interface FoodDslRepository {
    fun findAllFoodBySearch(pageable: Pageable, queryParams: MutableMap<String, String>): Page<Food>
}