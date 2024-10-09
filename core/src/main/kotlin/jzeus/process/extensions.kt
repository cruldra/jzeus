package jzeus.process

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import java.io.ByteArrayOutputStream
import java.util.*

fun Collection<ProcessHandle>.killAll() {
    forEach { it.destroy() }
}


fun Number.toPort(): Port = Port(toInt())


/**
 * 使用这个端口的进程列表
 */
val Port.progress: List<ProcessHandle>
    get() {
        val port = this.value
        val processes = mutableListOf<ProcessHandle>()
        val command = when {
            System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win") ->
                arrayOf("cmd", "/c", "netstat -ano | findstr :$port")

            else ->
                arrayOf("sh", "-c", "lsof -i :$port")
        }

        val process = Runtime.getRuntime().exec(command)
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val lines = output.split("\n")

        lines.forEach { line ->
            val parts = line.trim().split("\\s+".toRegex())
            if (parts.size >= 2) {
                val pid = parts.last().toIntOrNull()
                if (pid != null) {
                    val proc = ProcessHandle.of(pid.toLong()).orElse(null)
                    if (proc != null) {
                        processes.add(proc)
                    }
                }
            }
        }

        return processes
    }


fun PID.kill() {
    val killCommand = CommandLine("taskkill")
    killCommand.addArgument("/F")  // 强制终止
    killCommand.addArgument("/T")  // 终止进程树（包括子进程）
    killCommand.addArgument("/PID")
    killCommand.addArgument(this.value.toString())

    println("Executing: $killCommand")

    val killExecutor = DefaultExecutor.builder().get()
    val killOutputStream = ByteArrayOutputStream()
    killExecutor.streamHandler = PumpStreamHandler(killOutputStream)

    try {
        val exitValue = killExecutor.execute(killCommand)
        println("Taskkill output: ${killOutputStream.toString()}")
        println("Taskkill exit value: $exitValue")
    } catch (e: Exception) {
        println("Error killing processes: ${e.message}")
    } finally {
        killOutputStream.close()
    }
}




/*fun DefaultExecutor.getProcessId(): PID {
    val executeStreamHandler = ReflectUtil.getFieldValue(this ,"executeStreamHandler")  as ExecuteStreamHandler
    val watchdog = ReflectUtil.getFieldValue(this ,"watchdog")  as ExecuteWatchdog

    val processField = watchdog.javaClass.getDeclaredField("process")
    processField.isAccessible = true
    val process = processField.get(watchdog) as Process

    return PID(process.pid())  // 使用Java 9+的pid()方法
}*/

/*
fun main() {


    fun runProcessAndGetPid(executablePath: String): Long? {
        val kernel32 = Kernel32.INSTANCE

        val startupInfo = WinBase.STARTUPINFO()
        val processInfo = WinBase.PROCESS_INFORMATION()

        val success = kernel32.CreateProcess(
            executablePath,    // lpApplicationName
            null,              // lpCommandLine
            null,              // lpProcessAttributes
            null,              // lpThreadAttributes
            false,             // bInheritHandles
            WinDef.DWORD(WinNT.CREATE_NO_WINDOW.toLong()),  // dwCreationFlags
            Pointer.NULL,      // lpEnvironment
            null,              // lpCurrentDirectory
            startupInfo,       // lpStartupInfo
            processInfo        // lpProcessInformation
        )


        if (success) {
            val pid = processInfo.dwProcessId.toLong()
            println("Process started with PID: $pid")

            // 关闭句柄以防止资源泄漏
            kernel32.CloseHandle(processInfo.hProcess)
            kernel32.CloseHandle(processInfo.hThread)

            return pid
        } else {
            val error = kernel32.GetLastError()
            println("Failed to start process. Error code: $error")
            return null
        }
    }

    fun terminateProcess(pid: Long) {
        val kernel32 = Kernel32.INSTANCE
        val hProcess = kernel32.OpenProcess(WinNT.PROCESS_TERMINATE, false, pid.toInt())

        if (hProcess != null) {
            if (kernel32.TerminateProcess(hProcess, 1)) {
                println("Process with PID $pid terminated successfully")
            } else {
                val error = kernel32.GetLastError()
                println("Failed to terminate process. Error code: $error")
            }
            kernel32.CloseHandle(hProcess)
        } else {
            println("Failed to open process with PID $pid")
        }
    }

    val executablePath = "D:\\Program Files\\JianyingPro5.9.0\\JianyingPro.exe"
    val pid = runProcessAndGetPid(executablePath)

    if (pid != null) {
        // 等待一段时间
        Thread.sleep(5000) // 等待5秒

        // 终止进程
        terminateProcess(pid)
    }
}*/
