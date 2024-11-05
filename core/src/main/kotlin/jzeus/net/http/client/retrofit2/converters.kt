package jzeus.net.http.client.retrofit2

import jzeus.file.subFile
import jzeus.uuid.uuid
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * 这个转换器将响应内容读取为一个字符串
 *
 * ```kotlin
 *
 * interface Api {
 *     @GET("/api/v1/str")
 *     fun getFile(): Call<String>
 * }
 *
 * val api = retrofit {
 *     baseUrl("https://example.com")
 *     addConverterFactory(RawStringConverterFactory())
 * }.create(Api::class.java)
 *
 * val content:String = api.getFile().execute().body()
 * ```
 * @author dongjak
 * @created 2024/11/06
 * @version 1.0
 * @since 1.0
 */
class RawStringConverterFactory : Converter.Factory() {
    private class RawStringConverter : Converter<ResponseBody, String> {
        override fun convert(value: ResponseBody): String {
            return value.string()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type == String::class.java) {
            return RawStringConverter()
        }
        return null
    }
}


/**
 * 这个转换器将响应内容读取为一个文件
 *
 * ```kotlin
 * interface Api {
 *     @GET("/api/v1/file")
 *     fun getFile(): Call<File>
 * }
 *
 * val api = retrofit {
 *     baseUrl("https://example.com")
 *     addConverterFactory(FileConverterFactory())
 * }.create(Api::class.java)
 *
 * val file:File = api.getFile().execute().body()
 * ```
 *
 * @author dongjak
 * @created 2024/11/06
 * @version 1.0
 * @since 1.0
 */
class FileConverterFactory(private val dir: File = Files.createTempDirectory(uuid()).toFile()) :
    Converter.Factory() {

    private class FileConverter(val dir: File) : Converter<ResponseBody, File> {
        override fun convert(value: ResponseBody): File {
            val file = dir.subFile(uuid())
            Files.copy(value.byteStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            return file
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type == File::class.java) {
            return FileConverter(dir)
        }
        return null
    }
}
