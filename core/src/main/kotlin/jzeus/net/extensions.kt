package jzeus.net

import java.io.IOException
import java.net.ServerSocket


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

fun main() {
    print(isPortAvailable(3128))
}
