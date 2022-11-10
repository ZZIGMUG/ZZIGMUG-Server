package zzigmug.server.data

import zzigmug.server.entity.Food

data class FoodRequestDto(
    var name: String,
    var englishName: String,
    var calories: Int,
    var carbohydrate: Double,
    var protein: Double,
    var fat: Double
)

data class FoodResponseDto(
    var id: Long?,
    var name: String,
    var calories: Int,
    var carbohydrate: Double,
    var protein: Double,
    var fat: Double
) {
    constructor(food: Food): this(food.id, food.name, food.calories, food.carbohydrate, food.protein, food.fat)
}

data class FoodPageResponseDto(
    var totalCount: Long,
    var pageCount: Int,
    var data: MutableList<FoodResponseDto>
)