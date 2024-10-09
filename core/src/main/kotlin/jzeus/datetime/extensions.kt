package jzeus.datetime

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
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
