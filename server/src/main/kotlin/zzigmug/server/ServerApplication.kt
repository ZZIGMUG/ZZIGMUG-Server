package zzigmug.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import zzigmug.server.data.property.JwtProperty
import zzigmug.server.data.property.KakaoProperty

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(JwtProperty::class, KakaoProperty::class)
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
