package jzeus.io

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun InputStream.readToString(): String = bufferedReader().use { it.readText() }
/**
 * 将InputStream写入临时文件
 * @param prefix 临时文件名前缀，默认为"temp"
 * @param suffix 临时文件扩展名，默认为".tmp"
 * @param directory 临时文件目录，默认为系统临时目录
 * @param deleteOnExit 是否在JVM退出时删除临时文件，默认为true
 * @return 临时文件对象
 */
fun InputStream.writeToTempFile(
    prefix: String = "temp",
    suffix: String? = ".tmp",
    directory: File? = null,
    deleteOnExit: Boolean = true
): File = use { inputStream ->
    // 创建临时文件
    val tempFile = File.createTempFile(prefix, suffix, directory).apply {
        if (deleteOnExit) {
            deleteOnExit()
        }
    }

    // 将输入流写入临时文件
    Files.copy(
        inputStream,
        tempFile.toPath(),
        StandardCopyOption.REPLACE_EXISTING
    )

    tempFile
}
