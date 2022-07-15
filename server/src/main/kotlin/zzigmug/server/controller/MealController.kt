package zzigmug.server.controller

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import zzigmug.server.data.MealRequestDto
import zzigmug.server.service.MealService
import zzigmug.server.utils.exception.ResponseMessage
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/meal")
class MealController(
    private val mealService: MealService,
) {
    @PostMapping
    fun createMeal(@RequestBody requestDto: MealRequestDto, request: HttpServletRequest): ResponseEntity<Any> {
        val userId = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(mealService.createMeal(requestDto, userId))
    }

    @GetMapping
    fun readByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate, request: HttpServletRequest): ResponseEntity<Any> {
        val userId = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(mealService.readMealsByDate(date, userId))
    }

    @DeleteMapping
    fun deleteMeal(@RequestParam mealId: Long): ResponseEntity<Any> {
        mealService.deleteMeal(mealId)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}