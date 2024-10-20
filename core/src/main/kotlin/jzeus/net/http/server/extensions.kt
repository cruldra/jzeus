package jzeus.net.http.server

import cn.hutool.core.lang.ClassScanner
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import jzeus.cls.isSub
import jzeus.uuid.uuid
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun Context.resultString(result: String) {
    result(result)
    contentType("text/plain; charset=utf-8")
}

fun Context.resultBool(result: Boolean) {
    resultString(result.toString())
}

fun Context.resultJson(data: Any) {
    json(data)
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

fun Context.queryParamOrNull(name: String): String? = queryParam(name)?.trim()?.takeIf { it.isNotEmpty() }

/**
 * 注册路由定义,这个方法会扫描指定包下所有实现了[JavalinRouterDefinitions]接口的类,并注册到`Javalin`实例中
 * @param basePackage 要扫描的包名
 * @return 返回注册后的`Javalin`实例
 */
fun Javalin.registerRouters(basePackage: String): Javalin {
    val routerDefinitions = ClassScanner(basePackage) { clazz ->
        clazz.isSub(JavalinRouterDefinitions::class.java)
    }.scan()
    routerDefinitions.forEach { clazz ->
        val instance = clazz.getDeclaredConstructor().newInstance()
        clazz.declaredMethods.forEach { method ->
            val postMapping = method.getAnnotation(PostMapping::class.java)
            if (postMapping != null) {
                this.post(postMapping.value[0]) { ctx ->
                    method.invoke(instance, ctx)
                }
            }
            val getMapping = method.getAnnotation(GetMapping::class.java)
            if (getMapping != null) {
                this.get(getMapping.value[0]) { ctx ->
                    method.invoke(instance, ctx)
                }
            }
            val deleteMapping = method.getAnnotation(DeleteMapping::class.java)
            if (deleteMapping != null) {
                this.delete(deleteMapping.value[0]) { ctx ->
                    method.invoke(instance, ctx)
                }
            }
        }
    }
    return this
}
