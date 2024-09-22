package jzeus.str

import org.apache.commons.exec.CommandLine
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

fun main() {
    val urls = listOf(
        "https://www.example.com",
        "http://subdomain.example.co.uk:8080",
        "https://example.com/path/to/page?name=value&name2=value2",
        "invalid url",
        "ftp://example.com",
        "just.words"
    )

    urls.forEach { url ->
        println("$url is ${if (url.isUrl) "valid" else "invalid"}")
    }
}
