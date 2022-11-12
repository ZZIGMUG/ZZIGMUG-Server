package zzigmug.server.data

import com.fasterxml.jackson.annotation.JsonFormat
import zzigmug.server.data.type.MealType
import zzigmug.server.entity.Photo
import java.time.LocalDate
import java.time.LocalDateTime

data class PhotoRequestDto (
    val id: Long,
    val date: LocalDateTime,
    val mealType: MealType,
)

data class PhotoResponseDto (
    val id: Long?,
    val image: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val date: LocalDateTime?,
    val mealType: MealType?,
    val dishList: MutableList<DishResponseDto> = mutableListOf()
) {
    constructor(photo: Photo): this(photo.id, photo.image, photo.date, photo.mealType) {
        photo.dishList.forEach {
            this.dishList.add(DishResponseDto(it))
        }
    }
}

data class PhotoCalorieResponseDto (
    val date: LocalDate,
    val totalCalorie: Double,
    val breakfastCalorie: Double,
    val lunchCalorie: Double,
    val dinnerCalorie: Double,
)

data class PhotoNutrientResponseDto (
    var carbohydrate: Double = 0.0,
    var fat: Double = 0.0,
    var protein: Double = 0.0,
)