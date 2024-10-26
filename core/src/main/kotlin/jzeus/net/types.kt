package jzeus.net

import jzeus.progress.Progress
import java.net.Proxy
import java.time.Duration

data class Proxies(
    val http: Proxy? = null,
    val https: Proxy? = null,
    val socks: Proxy? = null,
)
enum class HttpMethod {
    POST,
    GET,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS;
}
/**
 * 文件下载选项
 * @property retryCount Int 失败重试次数,默认为1
 * @property progress Progress? 进度监视器
 * @property retryInterval Duration 每次失败重试的间隔时长,默认为1秒
 * @property async Boolean 是否启用异步下载
 * @property resume Boolean 是否启用断点续传
 * @property threadCount Int 异步下载时使用的线程的数量
 * @constructor
 */
data class FileDownloadOptions(
    val retryCount: Int = 1,
    val progress: Progress? = null,
    val retryInterval: Duration = Duration.ofSeconds(1),
    val async: Boolean = false,
    val resume: Boolean = true,
    val threadCount: Int = 1
)
data class DataRange(val start: Long, val end: Long) {
    var data: ByteArray? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataRange

        if (start != other.start) return false
        if (end != other.end) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}
