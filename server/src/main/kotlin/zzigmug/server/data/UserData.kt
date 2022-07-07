package zzigmug.server.data

import zzigmug.server.entity.User

data class UserInfo(
    var nickname: String? = null,
    var email: String,
    var height: Int? = null,
    var weight: Int? = null,
    var goal: Int? = null,
    var gender: GenderType? = null,
    var agree_marketing: Boolean,
    var exp: Long,
    var role: RoleType,
    var loginType: LoginType,
) {
    constructor(user: User): this(
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