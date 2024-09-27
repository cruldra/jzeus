package jzeus.os

import jzeus.any.print
import jzeus.any.truthValue
import jzeus.datetime.Timeout
import jzeus.datetime.Timeouts
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
    executor.execute(this)
    return logHandler.lines.toString()
}

fun main() {
    //print("powershell -Command echo hello".asCommandLine().exec())
    /* val proxyCommand = """
         powershell -Command '(Get-ItemProperty -Path "HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings").ProxyServer'
     """.trimIndent()
     println(proxyCommand.asCommandLine().exec())*/
//    """powershell -Command "Write-Output ((Get-ItemProperty -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings').ProxyServer)"""".asCommandLine()
//        .exec().print()
//    """powershell -Command "Write-Output ((Get-ItemProperty -Path 'HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings').ProxyEnable)"""".asCommandLine()
//        .exec().truthValue().print()
//    """powershell -ScriptBlock {(Get-ItemProperty -Path "HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings").ProxyServer}""".asCommandLine().exec().print()
//    """powershell -Command "(Get-ItemProperty -Path \"HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings\").ProxyServer"""".asCommandLine()
//        .exec().print()
//OS.isFamilyWindows()
//    System.getProperty("os.name").print()
//    System.getProperty("os.version").print()
//
    print(getSystemProxy())
}

fun getSystemProxy(): Proxies {
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
    TODO()
}

val OSVersion.matched: Boolean
    get() = System.getProperty("os.name") == this.value
