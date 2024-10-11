package jzeus.net.http.client.retrofit2

import cn.hutool.core.io.IoUtil
import jzeus.file.subFile
import jzeus.file.toFile
import jzeus.uuid.uuid
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

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
