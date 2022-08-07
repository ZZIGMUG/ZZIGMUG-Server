package zzigmug.server.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.FoodPage
import zzigmug.server.data.FoodRequestDto
import zzigmug.server.data.FoodResponseDto
import zzigmug.server.entity.Food
import zzigmug.server.repository.food.FoodRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class FoodService(
    private val foodRepository: FoodRepository,
) {

    @Transactional
    fun createFood(requestDto: FoodRequestDto): FoodResponseDto {

        if (foodRepository.existsByName(requestDto.name)) {
            throw CustomException(ResponseCode.FOOD_DUPLICATED)
        }

        val food = Food(requestDto)
        foodRepository.save(food)

        return FoodResponseDto(food)
    }

    @Transactional
    fun deleteFood(id: Long) {
        val food = foodRepository.findById(id).orElseThrow { throw CustomException(ResponseCode.FOOD_NOT_FOUND) }
        foodRepository.delete(food)
    }

    @Transactional(readOnly = true)
    fun readAll(pageable: Pageable, queryParams: MutableMap<String, String>): FoodPage {
        val foodList = mutableListOf<FoodResponseDto>()
        val pages = foodRepository.findAllFoodBySearch(pageable, queryParams)

        pages.forEach {
            foodList.add(FoodResponseDto(it))
        }

        return FoodPage(pages.totalElements, pages.totalPages, foodList)
    }
}