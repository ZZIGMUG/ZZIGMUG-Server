package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.repository.MealRepository

@Service
class MealService(
    private val mealRepository: MealRepository,
) {
}