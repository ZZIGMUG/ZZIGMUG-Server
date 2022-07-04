package zzigmug.server.data.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="kakao")
@ConstructorBinding
data class KakaoProperty(
    val restApiKey: String,
    val redirectUri: String,
)
