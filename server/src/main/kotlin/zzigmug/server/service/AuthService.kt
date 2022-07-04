package zzigmug.server.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import zzigmug.server.data.LoginResponseDto
import zzigmug.server.data.LoginType
import zzigmug.server.data.RoleType
import zzigmug.server.entity.User
import zzigmug.server.external.auth.KakaoOAuth2
import zzigmug.server.repository.UserRepository

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val kakaoOAuth2: KakaoOAuth2,
    private val authenticationManager: AuthenticationManager
){
    private final val ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC"

    fun kakaoLogin(authorizedCode: String): LoginResponseDto {
        val userInfo = kakaoOAuth2.getUserInfo(authorizedCode)
        var email = userInfo.email

        if (!userRepository.existsByEmail(email)) {
            val newUser = User(email = email, role = RoleType.ROLE_GUEST, loginType = LoginType.KAKAO)
            userRepository.save(newUser)
            return LoginResponseDto(newUser)
        }

        val user = userRepository.findByEmail(email)!!
        val token = UsernamePasswordAuthenticationToken(user.email, user.email+ADMIN_TOKEN)
        val authentication = authenticationManager.authenticate(token)
        SecurityContextHolder.getContext().authentication = authentication

        return LoginResponseDto(user)
    }

}