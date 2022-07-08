package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.data.FoodRequestDto
import zzigmug.server.data.FoodResponseDto
import zzigmug.server.entity.Food
import zzigmug.server.repository.FoodRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class FoodService(
    private val foodRepository: FoodRepository,
) {

    fun createFood(requestDto: FoodRequestDto): FoodResponseDto {
        val food = Food(requestDto)
        foodRepository.save(food)

        return FoodResponseDto(food)
    }

    fun deleteFood(id: Long) {
        val food = foodRepository.findById(id).orElseThrow { throw CustomException(ResponseCode.FOOD_NOT_FOUND) }
        foodRepository.delete(food)
    }

    fun readAll(): MutableList<FoodResponseDto> {
        val response = mutableListOf<FoodResponseDto>()
        foodRepository.findAll().forEach {
            response.add(FoodResponseDto(it))
        }

        return response
    }
}