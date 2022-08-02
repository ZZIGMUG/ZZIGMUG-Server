package zzigmug.server.data

import zzigmug.server.entity.Dish
import java.time.LocalDate

data class DishRequestDto (
    val foodId: Long,
    val amount: Float,
)

data class DishUpdateDto (
    val amount: Float,
)

data class DishResponseDto (
    val id: Long?,
    val foodId: Long?,
    val name: String,
    val calories: Float,
    val calbo: Float,
    val protein: Float,
    val fat: Float,
) {
    constructor(dish: Dish): this(
        dish.id,
        dish.food.id,
        dish.food.name,
        dish.food.calories * dish.amount,
        dish.food.calbo * dish.amount,
        dish.food.protein * dish.amount,
        dish.food.fat * dish.amount
        )
}

data class CalorieResponseDto (
    val date: LocalDate,
    val calorie: Double
)