package jzeus.net

import jzeus.any.print
import jzeus.os.exec
import jzeus.str.asCommandLine
import okhttp3.internal.platform.Platform
import java.io.IOException
import java.net.*


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

fun main() {
//    print(isPortAvailable(3128))
//    print(getSystemProxy("https://api.heygen.com/".toURI()))
    //print(System.getProperty("http.proxyHost"))
    //print("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /v ProxyServer".asCommandLine().exec())
"""(Get-ItemProperty -Path "HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings").ProxyServer""".asCommandLine().exec().print()
}
