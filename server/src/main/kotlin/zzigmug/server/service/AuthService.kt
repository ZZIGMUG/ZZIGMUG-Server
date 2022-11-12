package zzigmug.server.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zzigmug.server.config.jwt.JwtTokenProvider
import zzigmug.server.data.*
import zzigmug.server.data.type.LoginType
import zzigmug.server.data.type.RoleType
import zzigmug.server.entity.User
import zzigmug.server.external.auth.KakaoOAuth2
import zzigmug.server.repository.user.UserRepository
import zzigmug.server.utils.exception.CustomException
import zzigmug.server.utils.exception.ResponseCode

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val kakaoOAuth2: KakaoOAuth2,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
){
    fun emailJoin(requestDto: JoinEmailRequestDto): LoginResponseDto {
        if (userRepository.existsByEmail(requestDto.email)) throw CustomException(ResponseCode.USER_EMAIL_DUPLICATED)

        requestDto.password = passwordEncoder.encode(requestDto.password)
        val user = userRepository.save(User(requestDto))

        return LoginResponseDto(
            accessToken = createToken(user.email),
            refreshToken = null,
            userInfo = UserResponseDto(user)
        )
    }

    fun emailLogin(requestDto: LoginRequestDto): LoginResponseDto {
        val user = (userRepository.findByEmail(requestDto.email)) ?: throw CustomException(ResponseCode.LOGIN_FAIL)

        if (!passwordEncoder.matches(requestDto.password, user.pw))
            throw CustomException(ResponseCode.LOGIN_FAIL)

        return LoginResponseDto(
            accessToken = createToken(user.email),
            refreshToken = null,
            userInfo = UserResponseDto(user)
        )
    }

    fun getKakaoToken(authorizedCode: String): String {
        return kakaoOAuth2.getAccessToken(authorizedCode)
    }

    fun kakaoLogin(accessToken: String): LoginResponseDto {
        val userInfo = kakaoOAuth2.getUserInfoByAccessToken(accessToken)
        var email = userInfo.email
        val user = userRepository.findByEmail(email)

        // 신규 회원인 경우
        if (user == null) {
            val newUser = User(email = email, role = RoleType.ROLE_GUEST, loginType = LoginType.KAKAO, numberOfDays = 0)
            userRepository.save(newUser)
            return LoginResponseDto(
                accessToken = null,
                refreshToken = null,
                userInfo = UserResponseDto(newUser)
            )
        }

        return LoginResponseDto(
            accessToken = createToken(user.email),
            refreshToken = null,
            userInfo = UserResponseDto(user)
        )
    }

    fun kakaoJoin(requestDto: JoinRequestDto): LoginResponseDto {
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
            userInfo = UserResponseDto(user)
        )
    }

    @Transactional(readOnly = true)
    fun validateNickname(userInput: String) {
        val nickname = userInput.replace(" ", "")
        val exp = Regex("^[가-힣ㄱ-ㅎ ㅏ-ㅣ a-zA-Z0-9 -]{2,12}\$")

        if (!exp.matches(nickname)) {
            throw CustomException(ResponseCode.NICKNAME_INCORRECT)
        }

        if (userRepository.existsByNickname(nickname)) {
            throw CustomException(ResponseCode.NICKNAME_DUPLICATED)
        }
    }

    private fun createToken(userEmail: String): String {
        val authorities = ArrayList<String>()
        authorities.add("ROLE_USER")
        return jwtTokenProvider.getAccessToken(userEmail, authorities.toTypedArray())
    }


}