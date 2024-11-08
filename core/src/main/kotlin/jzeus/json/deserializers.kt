package jzeus.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import jzeus.any.Range
import java.io.IOException


/**
 * 从一个小写字符串反序列化为枚举
 *
 * ```kotlin
 * enum class TestEnum {
 *    A,B,C
 *  }
 * val objectMapper = ObjectMapperBuilder.build()
 * val json = "\"a\""
 * val enum = objectMapper.readValue(json, TestEnum::class.java)
 * log.info(enum) // A
 * ```
 */
class LowerCaseEnumDeserializer<T : Enum<T>> : JsonDeserializer<T>, ContextualDeserializer {
    private lateinit var enumType: Class<T>

    constructor() // 无参构造函数，用于Jackson的初始化

    constructor(enumType: Class<T>) { // 有参构造函数，用于手动初始化
        this.enumType = enumType
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val value = p.text.uppercase()
        return java.lang.Enum.valueOf(enumType, value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val contextClass = ctxt.contextualType.rawClass as Class<T>
        return LowerCaseEnumDeserializer(contextClass)
    }
}


/**
 * 从一个小写蛇形字符串反序列化为枚举
 *
 * ```kotlin
 * enum class TestEnum {
 *    A,B,C
 *  }
 * val objectMapper = ObjectMapperBuilder.build()
 * val json = "\"a_b\""
 * val enum = objectMapper.readValue(json, TestEnum::class.java)
 * log.info(enum) // A
 * ```
 */

class LowerSnakeCaseEnumDeserializer<T : Enum<T>> : JsonDeserializer<T>, ContextualDeserializer {
    private lateinit var enumType: Class<T>

    constructor() // 无参构造函数，用于Jackson的初始化

    constructor(enumType: Class<T>) { // 有参构造函数，用于手动初始化
        this.enumType = enumType
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val value = p.text.replace("-", "_").uppercase()
        return java.lang.Enum.valueOf(enumType, value)
    }

    @Suppress("UNCHECKED_CAST")
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val contextClass = ctxt.contextualType.rawClass as Class<T>
        return LowerSnakeCaseEnumDeserializer(contextClass)
    }
}
