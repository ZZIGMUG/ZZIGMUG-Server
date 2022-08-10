package zzigmug.server.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        val info = Info()
            .title("ZZIGMUG API")
            .version("1.0")
            .description("숙명여자대학교 19학번 졸업작품 찍먹")
            .termsOfService("http://swagger.io/terms/")
            .contact(Contact().name("Hyeonae Jang").url("https://github.com/raae7742").email("raae7742@gmail.com"))
            .license(License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))

        return OpenAPI().components(Components()).info(info)
    }
}