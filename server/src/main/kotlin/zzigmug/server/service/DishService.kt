package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.DishRequestDto
import zzigmug.server.data.DishResponseDto
import zzigmug.server.data.DishUpdateDto
import zzigmug.server.entity.Dish
import zzigmug.server.repository.DishRepository
import zzigmug.server.repository.food.FoodRepository
import zzigmug.server.repository.PhotoRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val photoRepository: PhotoRepository,
    private val foodRepository: FoodRepository,
) {
    @Transactional
    fun createDish(photoId: Long, requestDto: DishRequestDto): DishResponseDto {
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