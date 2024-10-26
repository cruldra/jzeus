package jzeus.datetime

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

val PopularDatetimeFormat.dateFormatter: SimpleDateFormat
    get() = SimpleDateFormat(this.value)

val PopularDatetimeFormat.dateTimeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern(this.value)

fun timestamp(): Long {
    return Date().time
}

fun Date.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
}

/**
 * 将当前时间加上一个超时时间
 *
 * ## 示例
 *
 * ```kotlin
 * val now = LocalDateTime.now() //2024/10/09 14:32:19
 * val newTime = now.plusTimeout(Timeout(1000)) //2024/10/09 14:32:20
 * val newTime = now.plusTimeout(Timeouts.minutes(10)) //2024/10/09 14:42:19
 * ```
 *
 * @param timeout 超时时间
 * @return 加上超时时间后的时间
 */
fun LocalDateTime.plusTimeout(timeout: Timeout): LocalDateTime {
    return this.plus(timeout.duration)
}

val LocalDateTime.timestamp: Long
    get() = this.toInstant(ZoneOffset.UTC).toEpochMilli()

/**
 * 使用流行的格式对这个日期时间进行格式化
 * @param format 格式
 * @return 格式化后的字符串
 */
fun LocalDateTime.formatPopular(format: PopularDatetimeFormat): String {
    return this.format(format.dateTimeFormatter)
}
fun Duration.gt(other: Duration) = this.toSeconds() > other.toSeconds()
