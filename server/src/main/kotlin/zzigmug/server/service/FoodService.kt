package zzigmug.server.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.FoodPageResponseDto
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
        return FoodResponseDto(foodRepository.save(Food(requestDto)))
    }

    @Transactional
    fun deleteFood(id: Long) {
        val food = foodRepository.findById(id).orElseThrow {
            throw CustomException(ResponseCode.FOOD_NOT_FOUND)
        }
        foodRepository.delete(food)
    }

    @Transactional(readOnly = true)
    fun readAll(pageable: Pageable, keyword: String?): FoodPageResponseDto {
        val pages = foodRepository.findAllFoodBySearch(pageable, keyword)

        val foodResponseList = mutableListOf<FoodResponseDto>()
        pages.forEach {
            foodResponseList.add(FoodResponseDto(it))
        }
        return FoodPageResponseDto(pages.totalElements, pages.totalPages, foodResponseList)
    }
}