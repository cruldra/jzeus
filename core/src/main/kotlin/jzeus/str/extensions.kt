package jzeus.str

import java.time.format.DateTimeFormatter

fun  String.toDateTimeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(this)
}
