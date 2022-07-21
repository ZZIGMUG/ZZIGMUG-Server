package zzigmug.server.data

import zzigmug.server.entity.User

data class UserInfo(
    var id: Long?,
    var nickname: String?,
    var email: String,
    var height: Int?,
    var weight: Int?,
    var goal: Int?,
    var gender: GenderType?,
    var agree_marketing: Boolean,
    var exp: Long,
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
        user.role,
        user.loginType
    )
}

data class UserPage(
    var totalCount: Long,
    var pageCount: Int,
    var data: MutableList<UserInfo>
)

data class UserFollowingDto(
    var id: Long?,
    var nickname: String?,
    var exp: Long,
) {
    constructor(user: User): this(user.id, user.nickname, user.exp)
}