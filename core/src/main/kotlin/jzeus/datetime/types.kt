package jzeus.datetime

import java.time.Duration

class Timeout(  val milliseconds: Long) {


    val duration: Duration
        get() = Duration.ofMillis(milliseconds)
    val seconds: Long
        get() = duration.seconds
    val minutes: Long
        get() = duration.toMinutes()

}

object Timeouts {
    val NEVER = Timeout(Long.MAX_VALUE)
    val ONE_SECOND = Timeout(1000)
    fun minutes(minutes: Long) = Timeout(minutes * 60 * 1000)
}

/**
 * 常用的日期时间格式
 */
enum class PopularDatetimeFormat(val value: String) {
    /**
     * 中国大陆常用的日期时间格式,示例:```2019-02-03 14:32:19```
     */
    CN_DATETIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * 标准的日期时间格式,包含时区信息,示例:```2019-02-03T14:32:19+08:00```
     */
    STANDARD_DATETIME("yyyy-MM-dd'T'HH:mm:ssXXX"),

    /**
     * 中国大陆常用的日期格式,示例:```2019-02-03```
     */
    CN_DATE("yyyy-MM-dd"),

    /**
     * 中国大陆常用的日期格式,示例:```2019-02-03```
     */
    CN_MONTH("yyyy-MM"),

    ISO8601_UTC("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
}
