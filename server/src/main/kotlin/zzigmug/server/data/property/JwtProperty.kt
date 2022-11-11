package zzigmug.server.data.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="jwt")
@ConstructorBinding
data class JwtProperty (
    val secret: String,
    val refresh: String,
    var prefix: String,
    var header: String,
)