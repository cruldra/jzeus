package jzeus.datetime

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

val PopularDatetimeFormat.dateFormatter: SimpleDateFormat
    get() = SimpleDateFormat(this.value)

val PopularDatetimeFormat.dateTimeFormatter: DateTimeFormatter
    get() = DateTimeFormatter.ofPattern(this.value)

fun timestamp(): Long {
    return Date().time
}
