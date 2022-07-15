package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.MealRequestDto
import zzigmug.server.data.MealResponseDto
import zzigmug.server.entity.Meal
import zzigmug.server.repository.MealRepository
import zzigmug.server.repository.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import java.time.LocalDate

@Service
class MealService(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository,
    private val dishService: DishService,
) {
    @Transactional
    fun createMeal(requestDto: MealRequestDto, userId: String): MealResponseDto {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val meal = Meal(requestDto, user)
        mealRepository.save(meal)

        requestDto.dishList.forEach {
            dishService.createDish(meal.id!!, it)
        }

        return MealResponseDto(meal)
    }

    @Transactional(readOnly = true)
    fun readMealsByDate(date: LocalDate, userId: String): MutableList<MealResponseDto> {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val response = mutableListOf<MealResponseDto>()

        mealRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59)).forEach {
            response.add(MealResponseDto(it))
        }
        return response
    }

    @Transactional(readOnly = true)
    fun readTodayMeals(userId: String): MutableList<MealResponseDto> {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val date = LocalDate.now()
        val response = mutableListOf<MealResponseDto>()

        mealRepository.findByUserAndDateBetween(user, date.atStartOfDay(), date.atTime(23, 59, 59)).forEach {
            response.add(MealResponseDto(it))
        }
        return response
    }

    @Transactional
    fun deleteMeal(mealId: Long) {
        val meal = mealRepository.findById(mealId).orElseThrow {
            throw CustomException(ResponseCode.MEAL_NOT_FOUND)
        }
        mealRepository.delete(meal)
    }
}