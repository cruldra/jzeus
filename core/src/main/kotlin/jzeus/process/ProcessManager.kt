package jzeus.process

import java.util.*

object ProcessManager {

    fun findProcessesByPort(port: Int): List<ProcessHandle> {
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


}

fun main() {
   print( ProcessManager.findProcessesByPort(11227)[0].destroy())
}
