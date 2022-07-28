package zzigmug.server.data

import com.fasterxml.jackson.annotation.JsonFormat
import zzigmug.server.entity.Photo
import java.time.LocalDateTime

data class PhotoRequestDto (
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val date: LocalDateTime,
    val image: String?,
    val dishList: List<DishRequestDto>
)

data class PhotoResponseDto (
    val id: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val date: LocalDateTime,
    val image: String?,
    val dishList: MutableList<DishResponseDto> = mutableListOf()
) {
    constructor(photo: Photo): this(photo.id, photo.date, photo.image) {
        photo.dishList.forEach {
            this.dishList.add(DishResponseDto(it))
        }
    }
}