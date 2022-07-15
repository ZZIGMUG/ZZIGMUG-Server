package zzigmug.server.data

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