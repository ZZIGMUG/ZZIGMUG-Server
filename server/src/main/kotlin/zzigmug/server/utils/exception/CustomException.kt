package zzigmug.server.utils.exception

import java.lang.RuntimeException

class CustomException(
    val errorCode: ResponseCode
): RuntimeException() {

}