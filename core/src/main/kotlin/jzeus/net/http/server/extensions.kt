package jzeus.net.http.server

import io.javalin.http.Context
import io.javalin.http.HttpStatus
import jzeus.uuid.uuid
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun <T> Context.jsonBody(clazz: Class<T>): T {
    return this.bodyAsClass(clazz)
}

fun Context.resultString(result: String) {
    result(result)
    contentType("text/plain; charset=utf-8")
}

fun Context.internalServerError(data: Any) {
    json(data)
    status(HttpStatus.INTERNAL_SERVER_ERROR)
}

fun Context.getFile(fileName: String = "file"): File? = uploadedFile(fileName)?.let { file ->
    // 创建一个临时文件来存储上传的音频
    //val tempFile = createTempFile(prefix = uuid(), suffix = ".${file.extension()}")
    //val tempFile = Files.createTempFile("audio_", ".${file.extension()}")
    val tempPath = Files.createTempFile(uuid(), ".${file.extension()}")
    try {
        // 将上传的文件内容复制到临时文件
        file.content().use { input ->
            Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING)
        }

        // 注册一个钩子，在 JVM 退出时删除临时文件
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                Files.deleteIfExists(tempPath)
            } catch (e: Exception) {
                // 日志记录删除失败
                // logger.error("Failed to delete temporary file: ${tempPath}", e)
            }
        })

        tempPath.toFile()
    } catch (e: Exception) {
        // 如果在复制过程中发生错误，删除临时文件并返回 null
        try {
            Files.deleteIfExists(tempPath)
        } catch (deleteException: Exception) {
            // 日志记录删除失败
            // logger.error("Failed to delete temporary file after error: ${tempPath}", deleteException)
        }
        null
    }
}
