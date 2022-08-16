package zzigmug.server.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.stereotype.Service
import zzigmug.server.data.property.AwsS3Property
import java.io.InputStream
import java.util.*

@Service
class AwsS3Service(
    private val awsS3Property: AwsS3Property,
    private val s3Client: AmazonS3Client
) {

    fun upload(inputStream: InputStream, originFileName: String, fileSize: Long): String {
        val s3FileName = UUID.randomUUID().toString() + "-" + originFileName
        val objMeta = ObjectMetadata()
        objMeta.contentLength = fileSize

        s3Client.putObject(awsS3Property.bucket, s3FileName, inputStream, objMeta)
        return s3Client.getUrl(awsS3Property.bucket, awsS3Property.dir + s3FileName).toString()
    }
}