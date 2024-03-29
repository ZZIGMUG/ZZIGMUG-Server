package zzigmug.server.data

import zzigmug.server.data.type.GenderType
import zzigmug.server.data.type.LoginType
import zzigmug.server.data.type.RoleType
import zzigmug.server.entity.User

data class UserUpdateRequestDto(
    var nickname: String,
)

data class UserResponseDto(
    var id: Long?,
    var nickname: String?,
    var email: String,
    var height: Int?,
    var weight: Int?,
    var goal: Int?,
    var gender: GenderType?,
    var agree_marketing: Boolean,
    var exp: Long,
    var numberOfDays: Int,
    var role: RoleType,
    var loginType: LoginType,
) {
    constructor(user: User): this(
        user.id,
        user.nickname,
        user.email,
        user.height,
        user.weight,
        user.goal,
        user.gender,
        user.agree_marketing,
        user.exp,
        user.numberOfDays,
        user.role,
        user.loginType
    )
}

data class UserPageResponseDto(
    var totalCount: Long,
    var pageCount: Int,
    var data: MutableList<UserResponseDto>
)

data class UserFollowingResponseDto(
    var id: Long?,
    var nickname: String?,
    var exp: Long,
    var numberOfDays: Int,
) {
    constructor(user: User): this(user.id, user.nickname, user.exp, user.numberOfDays)
}