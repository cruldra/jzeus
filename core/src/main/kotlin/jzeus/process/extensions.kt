package jzeus.process

import com.fasterxml.jackson.databind.node.ArrayNode
import jzeus.json.isJson
import jzeus.json.objectMapper
import jzeus.os.exec
import jzeus.str.asCommandLine
import java.util.*

fun Collection<ProcessHandle>.killAll() {
    forEach { it.destroy() }
}


fun Number.toPort(): Port = Port(toInt())
fun String.toProcessName(): ProcessName = ProcessName(this)


/**
 * 使用这个端口的进程列表
 */
val Port.progress: List<ProcessHandle>
    get() {
        val port = this.value
        val processes = mutableListOf<ProcessHandle>()
        val command = when {
            System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win") -> arrayOf(
                "cmd",
                "/c",
                "netstat -ano | findstr :$port"
            )

            else -> arrayOf("sh", "-c", "lsof -i :$port")
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

/**
 * 该进程名称对应的进程`pid`列表
 */
val ProcessName.pids: List<PID>
    get() {
        val cliOutput =
            """ powershell -Command "& { Get-Process -Name '*${this.value}*' | Select-Object Id,Name | ConvertTo-Json }" """//-AsArray } ps7才支持
                .asCommandLine().exec()
        return if (cliOutput.isNotBlank() && cliOutput.isJson()) {

            if (cliOutput.trim().startsWith("{")) {
                val pid = objectMapper.readTree(cliOutput)
                listOf(PID(pid.get("Id").asLong()))
            } else {
                val pids = objectMapper.readTree(cliOutput) as ArrayNode
                pids.map {
                    PID(it.get("Id").asLong())
                }
            }

        } else emptyList()

    }


fun PID.kill(): Boolean {
    val process = ProcessHandle.of(this.value).orElse(null)
    return process?.destroy() == true
}
