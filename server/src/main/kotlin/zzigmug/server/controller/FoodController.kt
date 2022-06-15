package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.FoodService

@RestController
class FoodController(
    private val foodService: FoodService,
) {
}