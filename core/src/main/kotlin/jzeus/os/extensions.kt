package jzeus.os

import jzeus.datetime.Timeout
import jzeus.datetime.Timeouts
import org.apache.commons.exec.*
import java.io.File

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
