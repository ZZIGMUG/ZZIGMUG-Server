package zzigmug.server.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Service
import zzigmug.server.external.auth.KakaoOAuth2
import zzigmug.server.repository.UserRepository

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val kakaoOAuth2: KakaoOAuth2,
    private val authenticationManager: AuthenticationManager
){
    fun kakaoLogin(authorizedCode: String) {

    }

}