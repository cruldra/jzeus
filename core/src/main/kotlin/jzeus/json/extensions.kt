package jzeus.json


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.victools.jsonschema.generator.*
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption
import jzeus.datetime.PopularDatetimeFormat
import jzeus.datetime.dateFormatter
import jzeus.datetime.dateTimeFormatter
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime


val objectMapper = ObjectMapper().apply {
    dateFormat = PopularDatetimeFormat.CN_DATETIME.dateFormatter
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val javaTimeModule = JavaTimeModule()
    val defaultLocalDateTimeFormatter = PopularDatetimeFormat.CN_DATETIME.dateTimeFormatter
    val defaultLocalDateFormatter = PopularDatetimeFormat.CN_DATE.dateTimeFormatter
    javaTimeModule.addSerializer(
        LocalDateTime::class.java, LocalDateTimeSerializer(defaultLocalDateTimeFormatter)
    )
    javaTimeModule.addDeserializer(
        LocalDateTime::class.java, LocalDateTimeDeserializer(defaultLocalDateTimeFormatter)
    )
    javaTimeModule.addSerializer(
        LocalDate::class.java, LocalDateSerializer(defaultLocalDateFormatter)
    )
    javaTimeModule.addDeserializer(
        LocalDate::class.java, LocalDateDeserializer(defaultLocalDateFormatter)
    )
    javaTimeModule.addSerializer(
        Duration::class.java, PrettyDurationSerializer()
    )
    registerModule(javaTimeModule)
    registerModule(ParameterNamesModule())
    registerModule(
        KotlinModule.Builder().withReflectionCacheSize(512).configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false).configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, true).configure(KotlinFeature.StrictNullChecks, false).build()
    )
    registerModule(Jdk8Module())
    configure(WRITE_DATES_AS_TIMESTAMPS, false)

    val module = SimpleModule()
    registerModule(module)
}


/**
 * 任意对象转换成`json`字符串
 */
fun Any.toJsonString(): String = objectMapper.writeValueAsString(this)

/**
 * `json`字符串转换成对象
 *
 * @param clazz 对象类型
 */
fun <T> String.toJavaObject(clazz: Class<T>): T = objectMapper.readValue(this, clazz)
fun <T> String.readList(clazz: Class<T>): List<T> =
    objectMapper.readValue(this, objectMapper.typeFactory.constructCollectionType(List::class.java, clazz))

/**
 * 检查字符串是否为JSON
 */
fun String.isJson(): Boolean = try {
    objectMapper.readTree(this)
    true
} catch (e: Exception) {
    false
}


fun Class<*>.toJsonSchema(): String {
    val jakartaValidationModule = JakartaValidationModule(
        JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
        JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
    )
    val configBuilder = SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
    /* configBuilder.forFields().withRequiredCheck {field->
         field.getAnnotation(org.jetbrains.annotations.NotNull::class.java) !=null
     }*/
    val config: SchemaGeneratorConfig =
        configBuilder
            .with(jakartaValidationModule)
            .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
            .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
            .build()

    val generator = SchemaGenerator(config)
    val jsonSchema: JsonNode = generator.generateSchema(this)
    return jsonSchema.toJsonString()
}
