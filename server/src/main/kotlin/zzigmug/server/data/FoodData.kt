package zzigmug.server.data

import zzigmug.server.entity.Food

data class FoodRequestDto(
    var name: String,
    var calories: Int,
    var carbohydrate: Int,
    var protein: Int,
    var fat: Int
)

data class FoodResponseDto(
    var id: Long?,
    var name: String,
    var calories: Int,
    var carbohydrate: Int,
    var protein: Int,
    var fat: Int
) {
    constructor(food: Food): this(food.id, food.name, food.calories, food.carbohydrate, food.protein, food.fat)
}

data class FoodPage(
    var totalCount: Long,
    var pageCount: Int,
    var data: MutableList<FoodResponseDto>
)