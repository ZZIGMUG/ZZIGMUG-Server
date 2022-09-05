package zzigmug.server.entity

import zzigmug.server.data.FoodRequestDto
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Food (
    @Column
    var name: String,

    @Column
    var englishName: String,

    @Column
    var calories: Int,

    @Column
    var carbohydrate: Double,

    @Column
    var fat: Double,

    @Column
    var protein: Double,

    ): BaseEntity() {
    constructor(requestDto: FoodRequestDto): this(
        name = requestDto.name,
        englishName = requestDto.englishName,
        calories = requestDto.calories,
        carbohydrate = requestDto.carbohydrate,
        fat = requestDto.fat,
        protein = requestDto.protein
    )
}