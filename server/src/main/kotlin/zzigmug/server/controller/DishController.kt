package zzigmug.server.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.DishRequestDto
import zzigmug.server.data.DishUpdateDto
import zzigmug.server.service.DishService
import zzigmug.server.utils.exception.ResponseMessage

@RestController
@RequestMapping("/dish")
class DishController(
    private val dishService: DishService,
) {

    @PostMapping
    fun createDish(@RequestParam mealId: Long, @RequestBody requestDto: DishRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(dishService.createDish(mealId, requestDto))
    }

    @PatchMapping
    fun editCount(@RequestParam dishId: Long, @RequestBody requestDto: DishUpdateDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(dishService.editDish(dishId, requestDto))
    }

    @DeleteMapping
    fun deleteDish(@RequestParam dishId: Long): ResponseEntity<Any> {
        dishService.deleteDish(dishId)
        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}