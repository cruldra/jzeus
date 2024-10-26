package jzeus.net

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.unit.DataSize
import cn.hutool.core.io.unit.DataSizeUtil
import jzeus.console.printToConsole
import jzeus.log.LoggerDelegate
import jzeus.progress.Progress
import jzeus.task.async
import jzeus.task.retry
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URI
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

private val log by LoggerDelegate()

/**
 * 获取本机上的一个空闲端口
 *
 * @return [Int] 返回一个可用的端口号
 */
fun availablePort() = ServerSocket(0).use { socket ->
    socket.localPort
}

/**
 * 检查指定端口是否可用
 * @param port 要检查的端口号
 * @return [Boolean] 如果端口可用，返回`true`,否则返回`false`
 */
fun isPortAvailable(port: Int): Boolean {
    var serverSocket: ServerSocket? = null
    try {
        // 尝试创建一个ServerSocket并绑定到指定端口
        serverSocket = ServerSocket(port)
        // 如果成功创建并绑定，说明端口可用
        return true
    } catch (e: IOException) {
        // 如果抛出IOException，通常意味着端口被占用
        return false
    } finally {
        // 确保关闭ServerSocket
        if (serverSocket != null) {
            try {
                serverSocket.close()
            } catch (e: IOException) {
                // 忽略关闭时的异常
            }
        }
    }
}

fun String.toURL(): java.net.URL {
    return this.toURI().toURL()
}

fun String.toURI(): java.net.URI {
    return URI(this)
}

fun URL.getQueryParameter(name: String): String? {
    val query = this.query ?: ""
    val queryPairs = query.split("&")
    for (pair in queryPairs) {
        val keyValue = pair.split("=")
        if (keyValue.size == 2 && keyValue[0] == name) {
            return keyValue[1]
        }
    }
    return null
}

fun HttpURLConnection.toCurl(body: String? = null): String {
    val builder = StringBuilder("curl -v ")

    // Method
    builder.append("-X ").append(this.requestMethod).append(" \\\n  ")

    // Headers
    for ((key, value1) in this.requestProperties) {
        builder.append("-H \"").append(key).append(":")
        for (value in value1) builder.append(" ").append(value)
        builder.append("\" \\\n  ")
    }

    // Body
    if (body != null) builder.append("-d '").append(body).append("' \\\n  ")

    // URL
    builder.append("\"").append(this.url).append("\"")
    return builder.toString()
}

/**
 * 打开到此链接的http连接并跟踪3xx跳转
 * @receiver URL
 * @param method HttpMethod 请求方法
 * @param timeout Int 连接超时
 * @param headers Map<String, Any> 使用附加头
 * @return HttpURLConnection
 */
fun URL.openConnection(
    method: HttpMethod = HttpMethod.GET,
    timeout: Int = 30,
    headers: Map<String, String> = emptyMap()
): HttpURLConnection {

    val connection = this.openConnection() as HttpURLConnection
    connection.connectTimeout = timeout * 1000
    connection.readTimeout = timeout * 1000
    connection.requestMethod = method.name
    headers.forEach { (k, v) ->
        connection.setRequestProperty(
            k,
            v
        )
    }
    log.info("打开连接: ${connection.toCurl()}")
    return async {
        if (arrayOf(301, 302).contains(connection.responseCode))
            URL(connection.getHeaderField("Location")).openConnection(method, timeout, headers)
        else connection
    }.get(30, TimeUnit.SECONDS)
}

fun URL.getDataSize(): DataSize =
    DataSize.ofBytes(this.openConnection(HttpMethod.HEAD, 30).contentLength.toLong())

/**
 * 计算每秒字节传输量并输出格式化字符串
 * @param startTime LocalDateTime 传输开始时间
 * @param loaded Long 截止目前为止已传输的字节大小
 */
fun calcBytesLoadSpeed(startTime: LocalDateTime, loaded: Long): String? {
    //速度是当前 已读字节数/秒数
    val transmissionSeconds = Duration.between(
        startTime,
        LocalDateTime.now()
    ).seconds  //传输持续时间  大于1秒时才开始计算传输速度
    if (transmissionSeconds > 0) {
        return DataSizeUtil.format(
            loaded / transmissionSeconds
        ) + " /s"
    }

    return null
}

private fun URL.copyPartToFile(
    destination: File,
    dataRange: DataRange,
    progress: Progress?,
    updateLocalFileToProgress: Boolean = true  //这个方法有可能会被重试块调用多次,如果每次都把本地文件的大小更新到下载进度中去会导致进度不准确
) {
    val existingFileSize: Long = destination.length()
    if (updateLocalFileToProgress) progress?.current?.addAndGet(existingFileSize)
    val downloadConnection = this.openConnection(
        HttpMethod.GET, 30, mapOf(
            "Range" to "bytes=${dataRange.start}-${dataRange.end}"
        )
    )
    var bytesDownloaded: Long = 0
    val transmissionStartTime = LocalDateTime.now()
    downloadConnection.inputStream.use { `is` ->
        FileOutputStream(destination, true).use { os ->
            val buffer = ByteArray(1024)
            var bytesCount: Int
            while (`is`.read(buffer).also { bytesCount = it } > 0) {
                os.write(buffer, 0, bytesCount)
                bytesDownloaded += bytesCount.toLong()
                progress?.current?.addAndGet(bytesCount.toLong())
                if (progress != null) progress.speed =
                    calcBytesLoadSpeed(transmissionStartTime, bytesDownloaded)
            }
        }
    }
    downloadConnection.disconnect()
}

/**
 * 将链接下载到本地文件,最多重试3次
 *
 * 用于下载此链接的http客户端默认参数请参考[FileDownloadOptions]
 * @receiver URL
 * @param destination File 本地文件
 *
 */
fun URL.copyToFile(destination: File) {
    copyToFile(destination, FileDownloadOptions(3))
}

/**
 * 下载链接到本地文件
 *
 * @receiver URL
 * @param destination File
 */
fun URL.copyToFile(
    destination: File,
    options: FileDownloadOptions
) {

    if (!destination.exists()) FileUtil.mkParentDirs(destination)
    val downloadUrl = this
    val remoteFileSize = getDataSize().toBytes()  //远程文件大小
    if (remoteFileSize == -1L) throw IOException("无法连接到远程文件")
    val existingFileSize: Long = destination.length()  //本地文件大小
    var downloadStart = if (options.resume) existingFileSize else 0L  //下载起始位,如果是续传模式则值为本地文件大小,否则为0
    if (existingFileSize > remoteFileSize) { //如果本地文件大于远程文件,则删除并重新下载
        FileUtil.del(destination)
        downloadStart = 0L
    }
    val fullRange = DataRange(downloadStart, remoteFileSize)
    if (options.progress != null) {
        options.progress.setTotal(remoteFileSize)
        options.progress.update(downloadStart)
    }
    if (options.resume && remoteFileSize - existingFileSize == 0L) return  //如果远程文件和本地文件大小相同则直接返回

    if (options.async) {

        var _finalThreadCount = options.threadCount

        /*
             调整工作线程的数量,避免多个线程下载重复的数据造成资源浪费
              分配到每个工作线程的下载量如果小于1M,则最终使用的线程数减1(除非线程数已经是1)
              */
        fun adjustThreadCount() {
            if (_finalThreadCount > 1 && (remoteFileSize - downloadStart) / _finalThreadCount < 1048576) {
                _finalThreadCount--
                adjustThreadCount()
            }
        }
        adjustThreadCount()

        val avg = (remoteFileSize - downloadStart) / _finalThreadCount  //平均每一份有多少

        val ranges = mutableListOf<DataRange>()
        repeat(_finalThreadCount) {
            var rangeStart = avg * (it) + downloadStart
            if (it > 0) rangeStart += (it)
            var rangeEnd = rangeStart + avg
            if (it == _finalThreadCount - 1) rangeEnd = remoteFileSize
            ranges.add(DataRange(rangeStart, rangeEnd))
        }

        data class FilePartDownloadInfo(
            val url: URL,
            val destination: File,
            val range: DataRange
        )

        val taskContents = ranges.mapIndexed { index, dataRange ->
            val tmpFile = File("${destination.parent}${File.separator}${destination.name}.tmp${index}")
            /*
            如果分区文件存在就先删除,但是这样会在下载一个超大文件时要求所有分区文件全部成功下载才行
            网络状况不好的情况下可能永远也下不下来，这样所谓的多线程下载就失去了意义,所以应该实现分区文件下载时支持断点续传
            或者分区文件本地大小如果和预期一样直接跳过,这样最起码可以保证下载成功了的分区文件不会因为大文件判定为下载失败而遭到删除
             */
            if (tmpFile.exists() && tmpFile.length() != dataRange.end - dataRange.start)
                tmpFile.delete()
            FilePartDownloadInfo(
                downloadUrl,
                tmpFile,
                dataRange,
            )
        }
        val executor: ExecutorService = Executors.newFixedThreadPool(_finalThreadCount)
        var future: Future<*>? = null

        try {
            if (taskContents.size > 1) {
                // 多任务下载
                val futures: MutableList<Future<*>> = ArrayList()

                for (partInfo in taskContents) {
                    val partFuture: Future<*> = executor.submit {
                        for (attempt in 1..options.retryCount) {
                            try {
                                downloadUrl.copyPartToFile(
                                    partInfo.destination,
                                    partInfo.range,
                                    options.progress,
                                    attempt == 1
                                )
                                return@submit
                            } catch (e: Exception) {
                                if (attempt == options.retryCount) {
                                    throw e
                                }
                                Thread.sleep(options.retryInterval.seconds * 1000)
                            }
                        }
                    }
                    futures.add(partFuture)
                }


                // 等待所有任务完成
                for (f in futures) {
                    f.get()
                }
            } else {
                // 单任务下载
                future = executor.submit {
                    for (attempt in 1..options.retryCount) {
                        try {
                            downloadUrl.copyPartToFile(
                                destination,
                                fullRange,
                                options.progress,
                                attempt == 1
                            )
                            return@submit
                        } catch (e: Exception) {
                            if (attempt == options.retryCount) {
                                throw e
                            }
                            Thread.sleep(options.retryInterval.seconds * 1000)
                        }
                    }
                }

                future.get() // 等待任务完成
            }


            // 所有下载完成后的处理
            //taskContents.sort { a, b -> java.lang.Long.compare(a.getRange().getStart(), b.getRange().getStart()) }
            taskContents.sortedBy {
                it.range.start
            }
            if (taskContents.size > 1 &&
                taskContents.stream()
                    .mapToLong { part -> part.destination.length() }
                    .sum() >= remoteFileSize
            ) {
                // 合并文件

                for (partInfo in taskContents) {
                    FileInputStream(partInfo.destination).use { `is` ->
                        FileOutputStream(destination, true).use { os ->
                            val buffer = ByteArray(1024)
                            var bytesCount: Int
                            while ((`is`.read(buffer).also { bytesCount = it }) > 0) {
                                os.write(buffer, 0, bytesCount)
                            }
                        }
                    }
                }


                // 删除临时文件
                for (partInfo in taskContents) {
                    FileUtil.del(partInfo.destination)
                }
            }
        } finally {
            executor.shutdown()
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow()
                }
            } catch (e: InterruptedException) {
                executor.shutdownNow()
            }
        }
    } else retry(options.retryCount, options.retryInterval.seconds) { times ->
        copyPartToFile(
            destination,
            DataRange(downloadStart, remoteFileSize),
            options.progress, times == 1
        )
    }
}
