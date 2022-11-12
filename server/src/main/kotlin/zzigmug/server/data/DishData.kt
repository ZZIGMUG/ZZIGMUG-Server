package zzigmug.server.data

import zzigmug.server.entity.Dish

data class DishRequestDto (
    val foodId: Long,
    val amount: Double,
)

data class DishUpdateRequestDto (
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