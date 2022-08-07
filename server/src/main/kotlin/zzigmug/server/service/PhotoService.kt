package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import zzigmug.server.data.*
import zzigmug.server.entity.Food
import zzigmug.server.entity.Photo
import zzigmug.server.repository.PhotoRepository
import zzigmug.server.repository.food.FoodRepository
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
    private val dishService: DishService,
) {
    @Transactional
    fun extractDishesFromPhoto(image: MultipartFile, time: LocalDateTime, userId: String): PhotoResponseDto {
        // image를 s3에 저장 -> url 리턴
        val imageUrl = ""
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val photo = Photo(user, time, imageUrl)
        photoRepository.save(photo)

        // image를 ML server로 보내 음식 추출
        val foodNames = listOf("바나나", "사과", "오렌지")

        val dishList = mutableListOf<DishResponseDto>()
        foodNames.forEach {
            val food = foodRepository.findByName(it) ?: foodRepository.save(Food(it, 0, 0, 0, 0))
            dishService.saveDish(photo.id!!, DishRequestDto(food.id!!, 1.0))
        }

        return PhotoResponseDto(photo)
    }

    @Transactional(readOnly = true)
    fun readPhotosByDate(date: LocalDate, userId: String): MutableList<PhotoResponseDto> {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val response = mutableListOf<PhotoResponseDto>()

        photoRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59)).forEach {
            response.add(PhotoResponseDto(it))
        }
        return response
    }

    @Transactional(readOnly = true)
    fun readTodayPhotos(userId: String): MutableList<PhotoResponseDto> {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val date = LocalDate.now()
        val response = mutableListOf<PhotoResponseDto>()

        photoRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59)).forEach {
            response.add(PhotoResponseDto(it))
        }
        return response
    }

    @Transactional
    fun deletePhoto(photoId: Long) {
        val photo = photoRepository.findById(photoId).orElseThrow {
            throw CustomException(ResponseCode.PHOTO_NOT_FOUND)
        }
        photoRepository.delete(photo)
    }
}