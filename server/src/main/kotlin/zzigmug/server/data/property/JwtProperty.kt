package zzigmug.server.data.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="jwt")
@ConstructorBinding
data class JwtProperty (
    val secretKey: String,
    val refreshKey: String,
    var bearerPrefix: String,
    var authorizationHeader: String,
)