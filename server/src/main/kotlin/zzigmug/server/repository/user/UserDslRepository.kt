package zzigmug.server.repository.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import zzigmug.server.entity.User

interface UserDslRepository {
    fun findAllUserBySearch(pageable: Pageable, queryParams: MutableMap<String, String>): Page<User>
}