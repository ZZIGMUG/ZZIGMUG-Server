package zzigmug.server.config

import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import zzigmug.server.config.jwt.*

@Configurable
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtTokenProvider: JwtTokenProvider,
): WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .cors().configurationSource(corsConfigurationSource())
        .and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
        .and()
            .formLogin().disable()
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

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowedMethods = listOf("POST", "GET", "PUT", "PATCH", "DELETE")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source;
    }
}