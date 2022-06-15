package zzigmug.server.service

import org.springframework.stereotype.Service
import zzigmug.server.repository.FoodRepository

@Service
class FoodService(
    private val foodRepository: FoodRepository,
) {
}