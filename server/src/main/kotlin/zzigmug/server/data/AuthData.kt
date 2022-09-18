package zzigmug.server.data

import zzigmug.server.data.type.GenderType

data class KakaoLoginRequestDto (
    var accessToken: String,
        )
data class LoginResponseDto(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var userInfo: UserInfo
)

data class JoinRequestDto (
    var email: String,
    var nickname: String,
    var height: Int,
    var weight: Int,
    var goal: Int,
    var gender: GenderType
)

data class EmailJoinRequestDto (
    var email: String,
    var password: String,
    var nickname: String,
    var height: Int,
    var weight: Int,
    var goal: Int,
    var gender: GenderType,
)

data class LoginRequestDto (
    var email: String,
    var password: String,
)