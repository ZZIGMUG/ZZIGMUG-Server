package zzigmug.server.controller

import org.springframework.web.bind.annotation.RestController
import zzigmug.server.service.DishService

@RestController
class DishController(
    private val dishService: DishService,
) {
}