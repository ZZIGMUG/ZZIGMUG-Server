package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.*
import zzigmug.server.entity.Dish
import zzigmug.server.repository.DishRepository
import zzigmug.server.repository.food.FoodRepository
import zzigmug.server.repository.PhotoRepository
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import java.time.Instant
import java.time.LocalDate

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val photoRepository: PhotoRepository,
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun saveDish(photoId: Long, requestDto: DishRequestDto): DishResponseDto {
        val food = foodRepository.findById(requestDto.foodId).orElseThrow {
            throw CustomException(ResponseCode.FOOD_NOT_FOUND)
        }
        val photo = photoRepository.findById(photoId).orElseThrow {
            throw CustomException(ResponseCode.PHOTO_NOT_FOUND)
        }

        val dish = Dish(photo = photo, food = food, amount = requestDto.amount)
        photo.dishList.add(dish)
        dishRepository.save(dish)

        return DishResponseDto(dish)
    }

    @Transactional(readOnly = true)
    fun getWeeklyCalories(email: String, date: LocalDate): MutableList<CalorieResponseDto> {
        val startDate = date.minusDays(7)
        val user = userRepository.findByEmail(email)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val dishes = dishRepository.findByUserAndCreateAtBetween(user, Instant.from(startDate), Instant.from(date.atTime(23, 59, 59)))

        val responseList = mutableListOf<CalorieResponseDto>()
        var calories = .0
        var currentDate = startDate

        dishes.forEach {
            val isEqualDate = it.createAt.isAfter(Instant.from(currentDate.atStartOfDay()))
                    && it.createAt.isBefore(Instant.from(currentDate.atTime(23, 59, 59)))

            if (isEqualDate) {
                calories += (it.food.calories * it.amount)
            }
            else {
                while (!isEqualDate) {
                    responseList.add(CalorieResponseDto(currentDate, calories))
                    currentDate = currentDate.plusDays(1)
                    calories = .0
                }
            }
        }

        while (!currentDate.isAfter(date)) {
            responseList.add(CalorieResponseDto(currentDate, .0))
            currentDate = currentDate.plusDays(1)
        }

        return responseList
    }

    @Transactional(readOnly = true)
    fun getNutrients(email: String, date: LocalDate): NutrientResponseDto {
        val user = userRepository.findByEmail(email)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val dishes = dishRepository.findByUserAndCreateAtBetween(user, Instant.from(date.atStartOfDay()), Instant.from(date.atTime(23, 59, 59)))

        val response = NutrientResponseDto(.0, .0, .0)
        dishes.forEach {
            response.carbohydrate += it.food.carbohydrate
            response.fat += it.food.fat
            response.protein += it.food.protein
        }

        return response
    }

    @Transactional
    fun editDish(dishId: Long, requestDto: DishUpdateDto) {
        val dish = dishRepository.findById(dishId).orElseThrow {
            throw CustomException(ResponseCode.DISH_NOT_FOUND)
        }
        dish.amount = requestDto.amount
        dishRepository.save(dish)
    }

    @Transactional
    fun deleteDish(dishId: Long) {
        val dish = dishRepository.findById(dishId).orElseThrow {
            throw CustomException(ResponseCode.DISH_NOT_FOUND)
        }
        dishRepository.delete(dish)
    }
}