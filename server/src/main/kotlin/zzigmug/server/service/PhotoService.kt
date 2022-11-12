package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
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
    private val restTemplate: RestTemplate,
) {

    //TODO: numberOfDays 증가 코드 필요
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
        print(imageUrl)
        val photo = Photo(null, null, imageUrl, null)
        photoRepository.save(photo)

        // image를 ML server로 보내 음식 추출
        val uri = "http://533b-35-237-152-198.ngrok.io/items/?imageUrl=$imageUrl"
        val responseEntity = restTemplate.getForEntity(uri, Array<String>::class.java)

        responseEntity.body!!.forEach {
            val food = foodRepository.findByEnglishName(it) ?: foodRepository.save(Food(it, it, 0, 0.0, 0.0, 0.0))
            dishService.createDish(photo.id!!, DishRequestDto(food.id!!, 1.0))
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
    fun readWeeklyCalories(userEmail: String, endDay: LocalDate): MutableList<PhotoCalorieResponseDto> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val startDay = endDay.minusDays(6)
        val photos = photoRepository.findByUserAndDateBetween(user, startDay.atStartOfDay(), endDay.atTime(23, 59, 59))

        val response = mutableListOf<PhotoCalorieResponseDto>()

        var currentDay = startDay
        var totalCalories = .0
        var breakfastCalories = .0
        var lunchCalories = .0
        var dinnerCalories = .0
        var index = 0

        loop@ while(!currentDay.isAfter(endDay)) {
            val photoDate = photos[index].date!!
            if (photoDate.isAfter(currentDay.atTime(23, 59, 59))) {
                response.add(PhotoCalorieResponseDto(currentDay, totalCalories, breakfastCalories, lunchCalories, dinnerCalories))

                currentDay = currentDay.plusDays(1)
                totalCalories = .0
                breakfastCalories = .0
                lunchCalories = .0
                dinnerCalories = .0
                continue
            }

            val isEqualDate = photoDate.isAfter(currentDay.atStartOfDay())
                    && photoDate.isBefore(currentDay.atTime(23, 59, 59))

            while (isEqualDate) {
                for (dish in photos[index].dishList) {
                    val calorie = dish.food.calories * dish.amount

                    when (photos[index].mealType) {
                        MealType.BREAKFAST -> breakfastCalories += calorie
                        MealType.LUNCH -> lunchCalories += calorie
                        MealType.DINNER -> dinnerCalories += calorie
                    }
                    totalCalories += calorie
                }

                if (++index >= photos.size) break@loop
            }
        }

        while (!currentDay.isAfter(endDay)) {
            response.add(PhotoCalorieResponseDto(currentDay, totalCalories, breakfastCalories, lunchCalories, dinnerCalories))
            currentDay = currentDay.plusDays(1)
            totalCalories = .0
            breakfastCalories = .0
            lunchCalories = .0
            dinnerCalories = .0
        }

        return response
    }

    @Transactional(readOnly = true)
    fun readNutrients(email: String, date: LocalDate): PhotoNutrientResponseDto {
        val user = userRepository.findByEmail(email)
            ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val photos = photoRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59))

        val response = PhotoNutrientResponseDto(.0, .0, .0)
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