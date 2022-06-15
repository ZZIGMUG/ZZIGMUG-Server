package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.MealService

@RestController
class MealController(
    private val mealService: MealService,
) {
}