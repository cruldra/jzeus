package jzeus.str

import cn.hutool.core.text.UnicodeUtil
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import org.apache.commons.exec.CommandLine
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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

/**
 * 对字符串进行`bcrypt`加密
 *
 * 要使用这个方法,需要添加以下依赖:
 *
 * ```xml
 * <dependency>
 *     <groupId>org.springframework.security</groupId>
 *     <artifactId>spring-security-crypto</artifactId>
 *     <version>6.2.0</version>
 * </dependency>
 * ```
 */
fun String.bcryptHash(): String = BCryptPasswordEncoder().encode(this)

/**
 *汉字转换为拼音，英文字符不变
 *
 * 要使用这个扩展函数,需要添加以下依赖:
 * ```xml
 *         <dependency>
 *             <groupId>com.belerweb</groupId>
 *             <artifactId>pinyin4j</artifactId>
 *             <version>2.5.1</version>
 *         </dependency>
 * ```
 */
fun String.toPinyin(): String {
    var pinyinName = ""
    val nameChar = this.toCharArray()
    val defaultFormat = HanyuPinyinOutputFormat()
    defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
    defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
    for (i in nameChar.indices) {
        if (nameChar[i].code > 128) {
            try {
                pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]
            } catch (e: BadHanyuPinyinOutputFormatCombination) {
                e.printStackTrace()
            }

        } else {
            pinyinName += nameChar[i]
        }
    }
    return pinyinName
}

/**
 * 转换为unicode字符串
 * @receiver 被转换的原字符串
 * @return 转换过后的unicode字符串
 */
fun String.toUnicode(): String = UnicodeUtil.toUnicode(this)


/**
 * 将此字符串视为一个unicode字符串并转换为普通字符串
 * @receiver 原unicode字符串
 * @return 转换过后的普通字符串
 */
fun String.decodeUnicode(): String = UnicodeUtil.toString(this)


/**
 * 从此字符串中删除所有匹配的[子字符串][substr]
 * @receiver String
 * @param substr 要删除的字符串
 * @return [String] 删除子串后的新字符串
 */
fun String.delete(substr: String) = this.replace(substr, "")


/**
 * 从此字符串中删除第一个匹配的[子字符串][substr]
 * @receiver String
 * @param substr 要删除的字符串
 * @return [String] 删除子串后的新字符串
 */
fun String.deleteFirst(substr: String) = this.replaceFirst(substr, "")
