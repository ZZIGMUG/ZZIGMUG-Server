package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import zzigmug.server.data.*
import zzigmug.server.data.type.MealType
import zzigmug.server.entity.Food
import zzigmug.server.entity.Photo
import zzigmug.server.repository.PhotoRepository
import zzigmug.server.repository.food.FoodRepository
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import java.time.LocalDate

@Service
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
    private val dishService: DishService,
    private val awsS3Service: AwsS3Service,
) {

    @Transactional
    fun savePhoto(requestDto: PhotoRequestDto, userId: String): PhotoResponseDto {
        val photo = photoRepository.findById(requestDto.id).orElseThrow {
            throw CustomException(ResponseCode.PHOTO_NOT_FOUND)
        }
        val user = userRepository.findByEmail(userId)?:
            throw CustomException(ResponseCode.USER_NOT_FOUND)

        photo.date = requestDto.date
        photo.mealType = requestDto.mealType
        photo.user = user
        photoRepository.save(photo)

        return PhotoResponseDto(photo)
    }

    @Transactional
    fun extractDishesFromPhoto(image: MultipartFile): PhotoResponseDto {
        val imageUrl = awsS3Service.upload(image.inputStream, image.originalFilename!!, image.size)
        val photo = Photo(null, null, imageUrl, null)
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

    @Transactional(readOnly = true)
    fun getWeeklyCalories(email: String, date: LocalDate): MutableList<CalorieResponseDto> {
        val startDate = date.minusDays(6)
        val user = userRepository.findByEmail(email)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val photos = photoRepository.findByUserAndDateBetween(user, startDate.atStartOfDay(), date.atTime(23, 59, 59))

        val responseList = mutableListOf<CalorieResponseDto>()

        var currentDate = startDate
        var calories = .0
        var breakfast = .0
        var lunch = .0
        var dinner = .0
        var index = 0

        loop@ while(!currentDate.isAfter(date)) {
            if (photos[index].date!!.isAfter(currentDate.atTime(23, 59, 59))) {
                responseList.add(CalorieResponseDto(currentDate, calories, breakfast, lunch, dinner))

                currentDate = currentDate.plusDays(1)
                calories = .0
                breakfast = .0
                lunch = .0
                dinner = .0
                continue
            }

            val isEqualDate = photos[index].date!!.isAfter(currentDate.atStartOfDay())
                    && photos[index].date!!.isBefore(currentDate.atTime(23, 59, 59))

            while (isEqualDate) {
                println(currentDate)
                for (dish in photos[index].dishList) {
                    val calorie = dish.food.calories * dish.amount
                    println(calorie)

                    when (photos[index].mealType) {
                        MealType.BREAKFAST -> breakfast += calorie
                        MealType.LUNCH -> lunch += calorie
                        MealType.DINNER -> dinner += calorie
                    }
                    calories += calorie
                }

                if (++index >= photos.size) break@loop
            }
        }

        while (!currentDate.isAfter(date)) {
            responseList.add(CalorieResponseDto(currentDate, calories, breakfast, lunch, dinner))
            currentDate = currentDate.plusDays(1)
            calories = .0
            breakfast = .0
            lunch = .0
            dinner = .0
        }

        return responseList
    }

    @Transactional(readOnly = true)
    fun getNutrients(email: String, date: LocalDate): NutrientResponseDto {
        val user = userRepository.findByEmail(email)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val photos = photoRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59))

        val response = NutrientResponseDto(.0, .0, .0)
        photos.forEach { photo ->
            photo.dishList.forEach { dish ->
                response.carbohydrate += dish.food.carbohydrate
                response.fat += dish.food.fat
                response.protein += dish.food.protein
            }
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