package jzeus.console

/**
 * 打印到控制台
 *
 * @param prefix 前缀
 * @param suffix 后缀
 * @param newline 是否换行
 */
fun <T> T.printToConsole(prefix: String = "", suffix: String = "", newline: Boolean = true) {
    println(
        "$prefix$this$suffix${if (newline) "\n" else ""}"
    )
}
