package jzeus.win32

import cn.hutool.core.io.resource.ClassPathResource
import jzeus.console.printToConsole
import jzeus.io.writeToTempFile
import jzeus.os.exec
import jzeus.str.asCommandLine

fun setWindowState(processName: String, state: WindowState) {
    """powershell -ExecutionPolicy Bypass -File "${
        ClassPathResource("win32/setWindowState.ps1").stream.writeToTempFile(
            "setWindowState",
            ".ps1"
        ).absolutePath
    }" -ProcessName "$processName" -State "${state.name}" """.asCommandLine()
        .exec().trim().printToConsole()
}
