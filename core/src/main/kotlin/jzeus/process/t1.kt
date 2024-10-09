package jzeus.process

import java.io.File
import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean
import kotlin.system.exitProcess

data class ProcessInfo(val pid: Int, val name: String)

class ProcessUtils {
    companion object {
        fun getProcessesByName(processName: String): List<ProcessInfo> {
            val matchingProcesses = mutableListOf<ProcessInfo>()
            val processDir = File("/proc")

            if (!processDir.exists() || !processDir.isDirectory) {
                println("This function is designed for Linux-like systems. It may not work on your current OS.")
                return emptyList()
            }

            processDir.listFiles { file -> file.isDirectory && file.name.all { it.isDigit() } }?.forEach { pidDir ->
                try {
                    val pid = pidDir.name.toInt()
                    val cmdlineFile = File(pidDir, "cmdline")
                    val processNameFromCmdline = cmdlineFile.readText().split('\u0000').firstOrNull() ?: ""
                    val processNameFromCmd = processNameFromCmdline.split("/").lastOrNull() ?: ""

                    if (processNameFromCmd.toLowerCase().contains(processName.toLowerCase())) {
                        matchingProcesses.add(ProcessInfo(pid, processNameFromCmd))
                    }
                } catch (e: Exception) {
                    // Ignore any errors (e.g., process no longer exists)
                }
            }

            return matchingProcesses
        }
    }
}

fun main() {
    val processName = "JianyingPro.exe" // 替换为你想要查找的进程名
    val processes = ProcessUtils.getProcessesByName(processName)

    if (processes.isEmpty()) {
        println("No processes found with name: $processName")
    } else {
        println("Found ${processes.size} processes with name: $processName")
        processes.forEach { process ->
            println("PID: ${process.pid}, Name: ${process.name}")
        }
    }
}
