package zzigmug.server.data

import zzigmug.server.entity.User
import javax.persistence.Column

data class LoginResponseDto(
    var id: Long?,
    var nickname: String? = null,
    var email: String?,
    var height: Long? = null,
    var weight: Long? = null,
    var goal: Long? = null,
    var gender: Boolean? = null,
    var agree_marketing: Boolean? = null,
    var exp: Long? = null,
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