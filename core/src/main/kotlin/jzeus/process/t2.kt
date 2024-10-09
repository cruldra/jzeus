package jzeus.process

import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Tlhelp32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT

data class ProcessInfo2(val pid: Int, val name: String)

class ProcessUtils2 {
    companion object {
        fun getProcessesByName(processName: String): List<ProcessInfo> {
            val processes = mutableListOf<ProcessInfo>()
            val kernel32 = Kernel32.INSTANCE
            val snapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, WinDef.DWORD(0))

            if (snapshot == WinNT.HANDLE.NULL) {
                throw RuntimeException("Failed to create snapshot")
            }

            try {
                val processEntry = Tlhelp32.PROCESSENTRY32.ByReference()
                processEntry.dwSize = processEntry.size()

                if (kernel32.Process32First(snapshot, processEntry)) {
                    do {
                        val currentProcessName = Native.toString(processEntry.szExeFile)
                        if (currentProcessName.toLowerCase().contains(processName.toLowerCase())) {
                            processes.add(ProcessInfo(processEntry.th32ProcessID.toInt(), currentProcessName))
                        }
                    } while (kernel32.Process32Next(snapshot, processEntry))
                }
            } finally {
                kernel32.CloseHandle(snapshot)
            }

            return processes
        }
    }
}

fun main() {
    val processName = "chrome" // 替换为你想要查找的进程名
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
