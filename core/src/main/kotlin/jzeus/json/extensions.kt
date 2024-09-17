package jzeus.json

import com.google.gson.Gson
import com.google.gson.JsonElement

fun <R> gson(block: Gson.() -> R): R {
    val gson = Gson()
    return block(gson)
}

/**
 * 任意对象转换成`json`字符串
 */
fun Any.toJsonString(): String = gson {
    toJson(this)
}

/**
 * `json`字符串转换成对象
 *
 * @param clazz 对象类型
 */
fun <T> String.toJavaObject(clazz: Class<T>): T {
    return gson {
        fromJson(this@toJavaObject, clazz)
    }
}

/**
 * 检查字符串是否为JSON
 */
fun String.isJson(): Boolean {
    return try {
        gson {
            fromJson(this@isJson, JsonElement::class.java)
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun main() {
    print("1".isJson())
}
