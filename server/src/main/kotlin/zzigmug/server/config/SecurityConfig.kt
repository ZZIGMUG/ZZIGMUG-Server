package zzigmug.server.config

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import zzigmug.server.config.jwt.*

@Configurable
@EnableWebSecurity
class SecurityConfig(
    private val corsConfig: CorsConfig,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtTokenProvider: JwtTokenProvider,
): WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
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
            .headers().frameOptions().disable()

            .and()
            .authorizeRequests()
            .antMatchers(
                "/h2-console/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "configuration/ui",
                "configuration/security",
                "/favicon.ico",
                "/webjars/**",
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/auth/**",
            ).permitAll()

            .and()
            .authorizeRequests()
            .antMatchers("/admin/**")
            .access("hasAnyRole('ROLE_ADMIN')")

            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()

            .and()
            .addFilterBefore(JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {}
}