package jzeus.core.json


import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.Duration
import java.util.*

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



/**
 * 序列化枚举为小写蛇形字符串
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
class LowerSnakeCaseEnumSerializer :
    StdSerializer<Enum<*>>(Enum::class.java, false) {
    override fun serialize(value: Enum<*>, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.name.lowercase(Locale.getDefault()).replace("_", "-"))
    }
}
