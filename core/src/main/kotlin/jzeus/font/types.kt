package jzeus.font

import java.awt.Font
import java.nio.file.Files
import java.nio.file.Paths


/**
 * 表示字体数据的类
 * @author dongjak
 * @created 2024/10/08
 * @version 1.0
 * @since 1.0
 */
data class FontData(
    val name: String, val family: String, val filePath: String
)

/**
 * 字体管理器
 * @author dongjak
 * @created 2024/10/08
 * @version 1.0
 * @since 1.0
 */
object FontManager {

    val CHINESE_FONTS_FILTER: (Font) -> Boolean = { it.canDisplay('中') }

    /**
     * 加载系统字体
     *
     * ## 示例1
     * ```kotlin
     * val fonts = FontManager.loadSystemFonts()
     * ```
     *
     * ## 获取中文字体
     * ```kotlin
     *  val fonts = FontManager.loadSystemFonts(FontManager.CHINESE_FONTS_FILTER)
     * ```
     *
     * @param filter 过滤器
     * @return 字体数据集合
     */
    fun loadSystemFonts(filter: (Font) -> Boolean = { true }): Set<FontData> {
        val fontDirectories = listOf(
            "C:\\Windows\\Fonts",
            "C:\\Users\\cruld\\AppData\\Local\\Microsoft\\Windows\\Fonts",
            "/System/Library/Fonts",
            "/usr/share/fonts"
        )
        val fonts = mutableSetOf<FontData>()

        for (dir in fontDirectories) {
            val path = Paths.get(dir)
            if (Files.exists(path)) {
                fonts.addAll(Files.walk(path).filter {
                    it.toString().endsWith(".ttf", ignoreCase = true) || it.toString()
                        .endsWith(".otf", ignoreCase = true)
                }.map { it.toFile() }.map { file ->
                    try {
                        val font = Font.createFont(
                            Font.TRUETYPE_FONT, file
                        )
                        if (filter(font)) FontData(
                            font.fontName, font.family, file.absolutePath
                        )
                        else null
                    } catch (e: Exception) {
                        null
                    }
                }.toList().filterNotNull().toSet())
            }
        }
        return fonts
    }

}
