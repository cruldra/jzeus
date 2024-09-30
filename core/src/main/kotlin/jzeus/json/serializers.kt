package jzeus.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import jzeus.any.Range
import java.io.IOException
import java.time.Duration
import java.util.*


/**
 * 这个格式化器主要目的是将[Duration]对象转换为一个更易读的字符串表示
 *
 * # 示例
 *
 * ```kotlin
 * val objectMapper = ObjectMapper().registerModule(
 *     SimpleModule().addSerializer(Duration::class.java, PrettyDurationSerializer())
 * )
 *
 * objectMapper.writeValueAsString(Duration.ofSeconds(123456789)) // 1d
 *
 * ```
 *
 * @author dongjak
 * @created 2024/09/26
 * @version 1.0
 * @since 1.0
 */
class PrettyDurationSerializer : JsonSerializer<Duration>() {
    override fun serialize(p0: Duration?, p1: JsonGenerator?, p2: SerializerProvider?) {
        p1?.writeString(
            Duration.ofSeconds(p0?.seconds ?: 0).toString()
                .substring(2)
                .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
                .lowercase(Locale.getDefault())
        )
    }
}

/**
 * 用`短横线命名法`序列化枚举
 *
 * ```kotlin
 * enum class TestEnum {
 *      A_B,C_D,E_F
 * }
 * val objectMapper = ObjectMapperBuilder.build()
 * val enum = TestEnum.A_B
 * val json = objectMapper.writeValueAsString(enum)
 * log.info(json) // "a-b"
 *  ```
 */
class KebabCaseEnumSerializer :
    StdSerializer<Enum<*>>(Enum::class.java, false) {
    override fun serialize(value: Enum<*>, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.name.lowercase(Locale.getDefault()).replace("_", "-"))
    }
}
/**
 * 序列化枚举为小写字符串
 *
 * ```kotlin
 * enum class TestEnum {
 *      A,B,C
 * }
 *  val objectMapper = ObjectMapperBuilder.build()
 *  val enum = TestEnum.A
 *  val json = objectMapper.writeValueAsString(enum)
 *  log.info(json) // "a"
 *  ```
 */
class LowerCaseEnumSerializer :
    StdSerializer<Enum<*>>(Enum::class.java, false) {
    override fun serialize(value: Enum<*>, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.name.lowercase(Locale.getDefault()))
    }
}


class RangeSerializer : JsonSerializer<Range<*>>() {
    @Throws(IOException::class)
    override fun serialize(
        src: Range<*>,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeStartArray()
        gen.writeString(src.min.toString())
        gen.writeString(src.max.toString())
        gen.writeEndArray()
    }
}
