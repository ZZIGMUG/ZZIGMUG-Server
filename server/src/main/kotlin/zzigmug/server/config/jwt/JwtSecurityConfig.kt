package zzigmug.server.config.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtSecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
): SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val filter = JwtFilter(jwtTokenProvider)
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
    }
}