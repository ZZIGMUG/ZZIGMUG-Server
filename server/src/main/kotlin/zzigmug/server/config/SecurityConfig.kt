package zzigmug.server.config

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import zzigmug.server.config.jwt.JwtAccessDeniedHandler
import zzigmug.server.config.jwt.JwtAuthenticationEntryPoint
import zzigmug.server.config.jwt.JwtSecurityConfig
import zzigmug.server.config.jwt.JwtTokenProvider

@Configurable
@EnableWebSecurity
class SecurityConfig(
    private val corsConfig: CorsConfig,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtTokenProvider: JwtTokenProvider,
): WebSecurityConfigurerAdapter() {

    companion object {
        val EXCLUDED_PATHS = arrayOf(
            "",
        )
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .addFilter(corsConfig.corsFilter())
            .cors().and()
            .csrf().disable()

            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .authorizeRequests()
            .antMatchers(*EXCLUDED_PATHS).permitAll()
            .antMatchers("/**")
            .access("hasRole('ROLE_USER')")
            .and()
            .apply(JwtSecurityConfig(jwtTokenProvider))
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
    }
}