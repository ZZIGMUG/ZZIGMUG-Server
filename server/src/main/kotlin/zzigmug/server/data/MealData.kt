package zzigmug.server.data

import com.fasterxml.jackson.annotation.JsonFormat
import zzigmug.server.entity.Meal
import java.time.LocalDateTime

data class MealRequestDto (
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val date: LocalDateTime,
    val image: String?,
    val dishList: List<DishRequestDto>
)

data class MealResponseDto (
    val id: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val date: LocalDateTime,
    val image: String?,
    val dishList: MutableList<DishResponseDto> = mutableListOf()
) {
    constructor(meal: Meal): this(meal.id, meal.date, meal.image) {
        meal.dishList.forEach {
            this.dishList.add(DishResponseDto(it))
        }
    }
}