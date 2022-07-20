package zzigmug.server.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.FoodRequestDto
import zzigmug.server.service.FoodService
import zzigmug.server.utils.exception.ResponseMessage

@RestController
@RequestMapping("/food")
class FoodController(
    private val foodService: FoodService,
) {

    @PostMapping
    fun createFood(@RequestBody requestDto: FoodRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(foodService.createFood(requestDto))
    }

    @GetMapping
    fun readFoodList(@RequestParam params: MutableMap<String, String>): ResponseEntity<Any> {
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(foodService.readAll(PageRequest.of(page, size), params))
    }

    @DeleteMapping
    fun deleteFood(@RequestParam foodId: Long): ResponseEntity<Any> {
        foodService.deleteFood(foodId)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }

}