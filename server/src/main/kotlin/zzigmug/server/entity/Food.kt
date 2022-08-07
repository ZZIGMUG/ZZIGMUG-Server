package zzigmug.server.entity

import zzigmug.server.data.FoodRequestDto
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Food (
    @Column
    var name: String,

    @Column
    var calories: Int,

    @Column
    var carbohydrate: Int,

    @Column
    var fat: Int,

    @Column
    var protein: Int,

    ): BaseEntity() {
    constructor(requestDto: FoodRequestDto): this(
        name = requestDto.name,
        calories = requestDto.calories,
        carbohydrate = requestDto.carbohydrate,
        fat = requestDto.fat,
        protein = requestDto.protein
    )
}