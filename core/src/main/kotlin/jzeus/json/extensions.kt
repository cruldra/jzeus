package jzeus.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import jzeus.any.Range
import jzeus.json.gson.RangeTypeAdapter

fun <R> gson(block: Gson.() -> R): R {
    val gson = GsonBuilder()
        .registerTypeAdapter(Range::class.java, RangeTypeAdapter())
        .create()
    return block(gson)
}

/**
 * 任意对象转换成`json`字符串
 */
fun Any.toJsonString(): String = gson {
    toJson(this@toJsonString)
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
