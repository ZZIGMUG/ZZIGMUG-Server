package zzigmug.server.data.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix="cloud.aws.s3")
@ConstructorBinding
data class AwsS3Property (
    val bucket: String,
    val dir: String,
)