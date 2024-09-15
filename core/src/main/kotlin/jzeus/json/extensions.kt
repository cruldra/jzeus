package jzeus.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import jzeus.str.toDateTimeFormatter
import java.io.File
import java.io.InputStream
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

fun jacksonObjectMapper(
    include: JsonInclude.Include = JsonInclude.Include.NON_NULL,
//        dateFormat: SimpleDateFormat = CHINESE_POPULAR_DATETIME.createDateFormatter(),
    failOnUnknownProperties: Boolean = false
) = ObjectMapper().apply {
//        setDateFormat(dateFormat)
    //序列化时不包含null或空属性
    setSerializationInclusion(include)
    //反序列化时如果找不到字段的writer不报错
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties)
    val javaTimeModule = JavaTimeModule()
    val defaultLocalDateTimeFormatter = "yyyy-MM-dd HH:mm:ss".toDateTimeFormatter()
    val defaultLocalDateFormatter = "yyyy-MM-dd".toDateTimeFormatter()
    javaTimeModule.addSerializer(
        LocalDateTime::class.java,
        LocalDateTimeSerializer(defaultLocalDateTimeFormatter)
    )
    javaTimeModule.addDeserializer(
        LocalDateTime::class.java,
        LocalDateTimeDeserializer(defaultLocalDateTimeFormatter)
    )
    javaTimeModule.addSerializer(
        LocalDate::class.java,
        LocalDateSerializer(defaultLocalDateFormatter)
    )
    javaTimeModule.addDeserializer(
        LocalDate::class.java,
        LocalDateDeserializer(defaultLocalDateFormatter)
    )
    javaTimeModule.addSerializer(
        Duration::class.java,
        PrettyDurationSerializer()
    )
    registerModule(javaTimeModule)
    registerModule(ParameterNamesModule())
    registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, true)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )
    registerModule(Jdk8Module())
    configure(WRITE_DATES_AS_TIMESTAMPS, false)
}

/**
 * 转换成json字符串
 * @receiver Any
 * @param pretty Boolean 是否格式化打印
 * @return String
 */
fun Any.toJsonString(pretty: Boolean = true): String = jacksonObjectMapper().let {
    if (pretty) it.writerWithDefaultPrettyPrinter() else it.writer()
}.writeValueAsString(this)

/**
 * 转换成`java`对象
 * @receiver Any
 * @param clazz Class<T> 目标对象类型
 * @return T
 */
fun <T> Any.toJavaObject(clazz: Class<T>): T {
    return jacksonObjectMapper().convertValue(this, clazz)
}


/**
 * jackson[JsonNode]转换成[Map]
 * @receiver JsonNode
 * @return Map<K, V>
 */
fun <K, V> JsonNode.toMap(): Map<K, V> =
    jacksonObjectMapper()
        .convertValue(this, object : TypeReference<Map<K, V>>() {})


/**
 * 检查字符串是否为JSON
 */
fun String.isJson(): Boolean {
    return try {
        ObjectMapper().readTree(this)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * 把`json`字符串转换成对象列表
 *
 * @param json json字符串
 * @param clazz 列表中的对象类型
 * @return 对象列表
 */
fun <T> ObjectMapper.readList(json: String, clazz: Class<T>): List<T> {
    return readValue(json, TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz))
}

fun <T> ObjectMapper.readArray(json: String, clazz: Class<T>): List<T> {
    return readValue(json, TypeFactory.defaultInstance().constructArrayType(clazz))
}

/**
 * 从[文件][file]中读取`json`字符串,然后将其转换成对象列表
 */
fun <T> ObjectMapper.readListFromFile(file: File, clazz: Class<T>) = readList(file.readText(), clazz)

/**
 * 从[文件路径][filePath]中读取`json`字符串,然后将其转换成对象列表
 */
fun <T> ObjectMapper.readListFromFile(filePath: String, clazz: Class<T>) = readListFromFile(File(filePath), clazz)


/**
 * 把`json`字符串转换成哈希表
 */
inline fun <reified K, reified V> ObjectMapper.readMap(json: String): Map<K, V> {
    return readValue(
        json,
        TypeFactory.defaultInstance().constructMapType(Map::class.java, K::class.java, V::class.java)
    )
}

inline fun <reified K, reified V> ObjectMapper.readMap(stream: InputStream): Map<K, V> {
    return readValue(
        stream,
        TypeFactory.defaultInstance().constructMapType(Map::class.java, K::class.java, V::class.java)
    )
}

inline fun <reified K, reified V> String.readAsJsonMap(): Map<K, V> {
    return jacksonObjectMapper().readMap(this)
}

inline fun <reified T> String.readAs(clazz: Class<T>): T {
    return jacksonObjectMapper().readValue(this, clazz)
}


/**
 * 假如有`json`如下:
 *
 * ```json
 * {"type": "executing", "data": {"node": null, "prompt_id": "cc304a5a-5897-40fd-9884-5c8a0ea18352"}}
 * ```
 *
 * 现在可以通过传入`data.prompt_id`来获取`prompt_id`的值
 *
 * ```kotlin
 * val json = ObjectMapperBuilder.build().readTree("{\"type\": \"executing\", \"data\": {\"node\": null, \"prompt_id\": \"cc304a5a-5897-40fd-9884-5c8a0ea18352\"}}")
 * val promptId = json.safeGet("data.prompt_id")?.asText()
 *
 * //promptId = "cc304a5a-5897-40fd-9884-5c8a0ea18352"
 * ```
 *
 */
fun JsonNode.safeGet(promptIdPath: String): JsonNode? {
    val pathSegments = promptIdPath.split('.')
    var currentNode: JsonNode? = this

    for (segment in pathSegments) {
        currentNode = currentNode?.path(segment)
        if (currentNode == null || currentNode.isMissingNode) {
            break
        }
    }

    return currentNode
}
