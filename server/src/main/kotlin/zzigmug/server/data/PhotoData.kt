package zzigmug.server.data

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import zzigmug.server.data.type.MealType
import zzigmug.server.entity.Photo
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