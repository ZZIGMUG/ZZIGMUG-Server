package zzigmug.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppContext {
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}