package zzigmug.server.data

import zzigmug.server.entity.Dish

data class DishRequestDto (
    val foodId: Long,
    val amount: Float,
)

data class DishUpdateDto (
    val amount: Float,
)

data class DishResponseDto (
    val id: Long?,
    val food: FoodResponseDto,
    val amount: Float,
) {
    constructor(dish: Dish): this(dish.id, FoodResponseDto(dish.food), dish.amount) {
        dish.food
    }
}