package zzigmug.server.repository.food

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import zzigmug.server.entity.Food

@Repository
interface FoodRepository: JpaRepository<Food, Long>, FoodDslRepository {
}