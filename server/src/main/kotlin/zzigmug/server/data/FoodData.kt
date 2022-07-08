package zzigmug.server.data

import zzigmug.server.entity.Food

data class FoodRequestDto(
    var name: String,
    var calories: Int,
    var calbo: Int,
    var protein: Int,
    var fat: Int
)

data class FoodResponseDto(
    var id: Long?,
    var name: String,
    var calories: Int,
    var calbo: Int,
    var protein: Int,
    var fat: Int
) {
    constructor(food: Food): this(food.id, food.name, food.calories, food.calbo, food.protein, food.fat)
}