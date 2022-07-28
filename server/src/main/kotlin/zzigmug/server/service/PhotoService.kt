package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.data.PhotoRequestDto
import zzigmug.server.data.PhotoResponseDto
import zzigmug.server.entity.Photo
import zzigmug.server.repository.PhotoRepository
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode
import java.time.LocalDate

@Service
class PhotoService(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
    private val dishService: DishService,
) {
    @Transactional
    fun savePhoto(requestDto: PhotoRequestDto, userId: String): PhotoResponseDto {
        val user = userRepository.findByEmail(userId)?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        val photo = Photo(requestDto, user)
        photoRepository.save(photo)

        requestDto.dishList.forEach {
            dishService.createDish(photo.id!!, it)
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