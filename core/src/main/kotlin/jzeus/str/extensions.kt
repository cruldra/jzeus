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

/**
 * 在追加字符串时,自动添加一个空格,通常用于构建命令行参数
 */
fun StringBuilder.appendWithSpace(str: String) {
    append(" $str")
}
