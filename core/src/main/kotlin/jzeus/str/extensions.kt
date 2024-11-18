package jzeus.str

import org.apache.commons.exec.CommandLine
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

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

/**
 * 判断字符串是否为`URL`
 */
val String.isUrl: Boolean
    get() {
        val urlPattern = Pattern.compile(
            "^(https?://)?" + // protocol
                    "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|" + // domain name
                    "((\\d{1,3}\\.){3}\\d{1,3}))" + // OR ip (v4) address
                    "(:\\d+)?(/[-a-z\\d%_.~+]*)*" + // port and path
                    "(\\?[;&a-z\\d%_.~+=-]*)?" + // query string
                    "(#[-a-z\\d_]*)?$", // fragment locator
            Pattern.CASE_INSENSITIVE
        )
        return urlPattern.matcher(this).matches()
    }

/**
 * 进行[URL编码](https://www.w3schools.com/tags/ref_urlencode.ASP)以处理特殊字符和非`ASCII`字符
 * @author dongjak
 * @created 2024/11/08
 * @version 1.0
 * @since 1.0
 */
fun String.urlEncode(): String = URLEncoder.encode(this, StandardCharsets.UTF_8)
fun String.toKebabCase(): String = this.replace("([a-z])([A-Z])".toRegex(), "$1-$2").lowercase()

fun String.firstLowerCase(): String = this.replaceFirstChar { it.lowercase() }
