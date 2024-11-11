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
class RangeDeserializer : JsonDeserializer<Range<*>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Range<*> {
        val node: JsonNode = p.codec.readTree(p)

        if (!node.isArray || node.size() != 2) {
            throw IOException("Invalid Range format. Expected an array with two elements.")
        }

        val minStr = node[0].asText()
        val maxStr = node[1].asText()

        // 尝试将字符串解析为不同的类型
        return when {
            isInteger(minStr) && isInteger(maxStr) -> {
                val min = minStr.toInt()
                val max = maxStr.toInt()
                Range(min, max)
            }

            isLong(minStr) && isLong(maxStr) -> {
                val min = minStr.toLong()
                val max = maxStr.toLong()
                Range(min, max)
            }

            isDouble(minStr) && isDouble(maxStr) -> {
                val min = minStr.toDouble()
                val max = maxStr.toDouble()
                Range(min, max)
            }

            else -> {
                // 如果无法解析为数字，则作为字符串处理
                Range(minStr, maxStr)
            }
        }
    }

    private fun isInteger(s: String): Boolean = s.toIntOrNull() != null
    private fun isLong(s: String): Boolean = s.toLongOrNull() != null
    private fun isDouble(s: String): Boolean = s.toDoubleOrNull() != null
}
