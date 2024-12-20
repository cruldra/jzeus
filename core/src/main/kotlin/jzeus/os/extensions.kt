package jzeus.os

import jzeus.any.truthValue
import jzeus.datetime.Timeout
import jzeus.datetime.Timeouts
import jzeus.failure.failure
import jzeus.net.Proxies
import jzeus.str.asCommandLine
import org.apache.commons.exec.*
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy

private class ExecLogHandler : LogOutputStream(1) {
    val lines = StringBuilder("")
    override fun processLine(line: String?, logLevel: Int) {
        lines.append(line).append("\n")
    }
}


fun CommandLine.exec(workDir: File? = null, timeout: Timeout = Timeouts.NEVER): String {
    val executor = DefaultExecutor.Builder()
        .setWorkingDirectory(workDir)
        .get()
    executor.setExitValues(null)
    val watchdog = ExecuteWatchdog.Builder()
        .setTimeout(timeout.duration)
        .get()
    executor.watchdog = watchdog
    val logHandler = ExecLogHandler()
    val streamHandler = PumpStreamHandler(logHandler)
    executor.streamHandler = streamHandler
    val retCode = executor.execute(this)
    return if (retCode == 0)
        logHandler.lines.toString()
    else failure("命令执行失败,退出码为:${retCode},错误详情:${logHandler.lines}")
}

fun getSystemProxy(): Proxies? {
    if (OSVersion.WIN11.matched) {
        val proxyIsEnabled =
            """powershell -Command "Write-Output ((Get-ItemProperty -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings').ProxyEnable)"""".asCommandLine()
                .exec().truthValue()
        val proxy =
            """powershell -Command "Write-Output ((Get-ItemProperty -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings').ProxyServer)"""".asCommandLine()
                .exec().split(":")
                .let {
                    Proxy(Proxy.Type.HTTP, InetSocketAddress(it[0].trim(), it[1].trim().toInt()))
                }
        return Proxies(
            http = if (proxyIsEnabled) proxy else null,
            https = if (proxyIsEnabled) proxy else null,
        )

    }
    return null
}

val OSVersion.matched: Boolean
    get() = System.getProperty("os.name") == this.value
