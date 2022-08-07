package zzigmug.server.data

import zzigmug.server.entity.Dish
import java.time.LocalDate

data class DishRequestDto (
    val foodId: Long,
    val amount: Double,
)

data class DishUpdateDto (
    val amount: Double,
)

data class DishResponseDto (
    val id: Long?,
    val foodId: Long?,
    val name: String,
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double,
) {
    constructor(dish: Dish): this(
        dish.id,
        dish.food.id,
        dish.food.name,
        dish.food.calories * dish.amount,
        dish.food.carbohydrate * dish.amount,
        dish.food.protein * dish.amount,
        dish.food.fat * dish.amount
        )
}

data class CalorieResponseDto (
    val date: LocalDate,
    val calorie: Double
)

data class NutrientResponseDto (
    var carbohydrate: Double = 0.0,
    var fat: Double = 0.0,
    var protein: Double = 0.0,
)