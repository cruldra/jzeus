package jzeus.str

import org.apache.commons.exec.CommandLine
import java.time.format.DateTimeFormatter

fun String.toDateTimeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(this)
}

fun stringBuilder(str: String, block: StringBuilder.() -> Unit = {}): StringBuilder {
    return StringBuilder(str).apply(block)
}

fun String.asCommandLine(): CommandLine = CommandLine.parse(this)
fun StringBuilder.appendWithSpace(str: String) {
    append(" $str")
}
