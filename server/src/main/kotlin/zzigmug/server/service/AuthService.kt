package zzigmug.server.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.config.jwt.JwtTokenProvider
import zzigmug.server.data.*
import zzigmug.server.entity.User
import zzigmug.server.external.auth.KakaoOAuth2
import zzigmug.server.repository.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val kakaoOAuth2: KakaoOAuth2,
    private val jwtTokenProvider: JwtTokenProvider,
){

    fun kakaoLogin(authorizedCode: String): LoginResponseDto {
        val userInfo = kakaoOAuth2.getUserInfo(authorizedCode)
        var email = userInfo.email
        val user = userRepository.findByEmail(email)

        // 신규 회원인 경우
        if (user == null) {
            val newUser = User(email = email, role = RoleType.ROLE_GUEST, loginType = LoginType.KAKAO)
            userRepository.save(newUser)
            return LoginResponseDto(
                accessToken = null,
                refreshToken = null,
                userInfo = UserInfo(newUser)
            )
        }

        return LoginResponseDto(
            accessToken = createToken(user.email),
            refreshToken = null,
            userInfo = UserInfo(user)
        )
    }

    fun join(requestDto: JoinRequestDto): LoginResponseDto {
        val user = userRepository.findByEmail(requestDto.email)?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        user.nickname = requestDto.nickname
        user.gender = requestDto.gender
        user.goal = requestDto.goal
        user.height = requestDto.height
        user.weight = requestDto.weight
        user.role = RoleType.ROLE_USER
        userRepository.save(user)

        return LoginResponseDto(
            accessToken = createToken(user.email),
            refreshToken = null,
            userInfo = UserInfo(user)
        )
    }

    @Transactional(readOnly = true)
    fun validateNickname(input: String): ResponseCode {
        val nickname = input.replace(" ", "")
        val exp = Regex("^[가-힣ㄱ-ㅎ ㅏ-ㅣ a-zA-Z0-9 -]{2,12}\$")
        if (!exp.matches(nickname)) return ResponseCode.USER_NICKNAME_INCORRECT

        if (userRepository.existsByNickname(nickname)) return ResponseCode.USER_NICKNAME_DUPLICATED
        return ResponseCode.OK
    }

    private fun createToken(email: String): String {
        val authorities = ArrayList<String>()
        authorities.add("ROLE_USER")
        return jwtTokenProvider.getAccessToken(email, authorities.toTypedArray())
    }


}