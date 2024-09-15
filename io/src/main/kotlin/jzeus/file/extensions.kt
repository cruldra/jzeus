package jzeus.file

import jzeus.failure.Codes.FILE_NOT_FOUND_CODE
import jzeus.failure.failure
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

val File.notFoundMessage: String
    get() = "不存在${absolutePath}这个文件"

fun String.asFile(block: File.() -> Unit = {}) = File(this).apply(block)
fun String.asDirectory() = Directory(this)
fun File.createIfNotExists(): File = if (exists()) this else apply { createNewFile() }
fun File.raiseIfNotExists(): File =
    if (exists()) this else failure(notFoundMessage, FILE_NOT_FOUND_CODE)

/**
 * 复制此文件到目标文件,如果此文件是一个目录,则复制该目录下的所有文件
 * @param destination 目标文件
 * @return [File] 目标文件
 */
fun File.copyTo(destination: File, override: Boolean = false): File {
    val options = mutableListOf<StandardCopyOption>()
    if (override) {
        options.add(StandardCopyOption.REPLACE_EXISTING)
    }
    if (!this.exists()) failure<Any>(notFoundMessage, FILE_NOT_FOUND_CODE)
    if (!destination.exists()) {
        destination.mkdirs()
    }
    if (this.isDirectory)
        this.listFiles()?.forEach { file ->
            val destFile = File(destination, file.name)
            if (!destFile.exists()) {
                if (file.isDirectory) {
                    file.copyTo(destFile)
                } else {
                    Files.copy(file.toPath(), destFile.toPath(), *options.toTypedArray()).toFile()
                }
            }
        }
    else if (!destination.exists())
        Files.copy(this.toPath(), destination.toPath(), *options.toTypedArray()).toFile()
    return destination
}

/**
 * 复制到同级目录并重命名
 * @param newName 新文件名
 * @return [File] 新文件
 */
fun File.copyTo(newName: String, override: Boolean = false) = copyTo(File(parent, newName), override)
fun main() {
    print("D:\\360Downloads".asFile().copyTo("360Downloads1").absolutePath)
}
