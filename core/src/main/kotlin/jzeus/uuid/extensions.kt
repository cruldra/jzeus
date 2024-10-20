package jzeus.uuid

import java.util.*


/**
 * 生成一个随机的UUID
 *
 * @param upper 是否大写
 * @param formats 格式化方式,例如:[(8, "-"), (12, "-"), (16, "-"), (20, "-")]
 * @return UUID字符串
 */
fun uuid(upper: Boolean = false, formats: List<Pair<Int, String>>? = null): String {
    var randomUuid = UUID.randomUUID().toString().replace("-", "")

    if (upper) {
        randomUuid = randomUuid.uppercase(Locale.getDefault())
    }

    if (formats != null) {
        val formattedUuid = StringBuilder()
        var start = 0
        for ((end, sep) in formats) {
            formattedUuid.append(randomUuid.substring(start, end))
            formattedUuid.append(sep)
            start = end
        }
        formattedUuid.append(randomUuid.substring(start))
        return formattedUuid.toString()
    } else {
        return randomUuid
    }
}
