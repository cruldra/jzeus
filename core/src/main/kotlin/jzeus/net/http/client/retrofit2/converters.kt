package jzeus.net.http.client.retrofit2

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

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
