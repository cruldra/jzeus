package jzeus.os

import org.apache.commons.exec.*
import java.io.File
import java.time.Duration

private class ExecLogHandler : LogOutputStream(1) {
    val lines = StringBuilder("")
    override fun processLine(line: String?, logLevel: Int) {
        lines.append(line).append("\n")
    }
}


fun CommandLine.exec(workDir: File? = null, timeout: Duration = Duration.ofMinutes(1)): String {
    val executor = DefaultExecutor.Builder()
        .setWorkingDirectory(workDir)
        .get()
    executor.setExitValues(null)
    val watchdog = ExecuteWatchdog.Builder()
        .setTimeout(timeout)
        .get()
    executor.watchdog = watchdog
    val logHandler = ExecLogHandler()
    val streamHandler = PumpStreamHandler(logHandler)
    executor.streamHandler = streamHandler
    executor.execute(this)
    return logHandler.lines.toString()
}
