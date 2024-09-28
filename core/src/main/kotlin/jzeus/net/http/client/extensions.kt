package jzeus.net.http.client

import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import jzeus.failure.failure
import jzeus.json.objectMapper
import jzeus.log.LoggerDelegate
import jzeus.net.http.client.retrofit2.RawStringConverterFactory
import jzeus.os.getSystemProxy
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

private val log by LoggerDelegate()
fun OkHttpClient.Builder.autoDetectProxy(): OkHttpClient.Builder = apply {
    proxy(getSystemProxy().http)
}

fun OkHttpClient.Builder.addExceptionInterceptor(block: Response.() -> Pair<Int, String>): OkHttpClient.Builder =
    apply {
        addInterceptor(ExceptionInterceptor(block))
    }

fun OkHttpClient.Builder.addInterceptorBefore(
    beforeClass: Class<out Interceptor>,
    interceptor: Interceptor
): OkHttpClient.Builder =
    apply {
        val interceptors = this.interceptors()
        interceptors.add(interceptors.indexOfFirst {
            it.javaClass.name == beforeClass.name
        }, interceptor)
    }

fun createHttpClient(block: OkHttpClient.Builder.() -> Unit = {}) = OkHttpClient.Builder().apply {
    callTimeout(Duration.ofMinutes(1))
    readTimeout(Duration.ofMinutes(5))
    connectTimeout(Duration.ofSeconds(30))
    addInterceptor(CurlInterceptor(object : Logger {
        override fun log(message: String) {
            log.info(message)
        }
    }))
}.apply(block).build()

fun retrofit(block: Retrofit.Builder.() -> Unit = {}): Retrofit = Retrofit.Builder().apply {
    client(createHttpClient())
    addConverterFactory(RawStringConverterFactory())
    addConverterFactory(JacksonConverterFactory.create(objectMapper))
}.apply(block).build()

fun <T, R> Retrofit.proxy(clazz: Class<T>, block: T.() -> R): R {
    val service = create(clazz)
    return block(service)
}

/**
 * 响应状态码不为`200`时,抛出异常
 *
 * @param block 响应状态码不为`200`时,指示如何从响应中获取错误代码和错误消息
 */
fun Response.raiseForStatus(block: Response.() -> Pair<Int, String>) {
    if (!isSuccessful) {
        val (code, message) = block()
        failure<Any>(message, code)
    }
}


val RequestBody.contentString: String
    get() = Buffer().also {
        this.writeTo(it)
    }.readUtf8()
