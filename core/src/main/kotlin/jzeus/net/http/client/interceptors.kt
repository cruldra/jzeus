package jzeus.net.http.client

import jzeus.crypto.decryptAES
import jzeus.crypto.encryptAES
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 这个异常拦截器会在响应码不是`200`时抛出异常
 * @param block 指示如何从响应中获取`错误码`和`错误信息`
 * @author dongjak
 * @created 2024/09/28
 * @version 1.0
 * @since 1.0
 */
class ExceptionInterceptor(private val block: Response.() -> Pair<Int, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        response.raiseForStatus(block)
        return response
    }
}


/**
 * 有些接口需要授权才能访问,通常是一个`Authorization`头,这个拦截器用于添加这个头
 * @author dongjak
 * @created 2024/09/28
 * @version 1.0
 * @since 1.0
 */
class AuthorizationHeaderInterceptor(private val value: String, private val name: String = "Authorization") :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header(name, value)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
/**
 * 这个拦截器使用`aes`在请求之前和响应之后对`请求体`和`响应体`进行加密和解密
 * @author dongjak
 * @created 2024/09/28
 * @version 1.0
 * @since 1.0
 */
class AesCryptoInterceptor(private val secretKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBody = originalRequest.body
        val mediaType = requestBody?.contentType()
        val requestBuilder = originalRequest.newBuilder()
        var response: Response? = null
        if (mediaType != null && mediaType.type == "application"
            && mediaType.subtype == "json"
        ) {
            requestBuilder.method(
                originalRequest.method, requestBody.contentString.encryptAES(secretKey)
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            )
            response = chain.proceed(requestBuilder.build())
        } else
            response = chain.proceed(originalRequest)

        return response.newBuilder().body(response.body?.string()?.decryptAES(secretKey)?.toResponseBody(mediaType))
            .build()
    }
}
