package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.repository.DishRepository

@Service
class DishService(
    private val dishRepository: DishRepository,
) {
}