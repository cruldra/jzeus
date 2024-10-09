package jzeus.jianying

import cn.hutool.core.util.ReflectUtil
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinBase
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import jzeus.datetime.Timeouts
import jzeus.os.exec
import jzeus.process.PID
import jzeus.process.kill
import jzeus.str.asCommandLine
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.ExecuteStreamHandler
import org.apache.commons.exec.ExecuteWatchdog
import java.io.File


interface ClickniumService {
    fun click(locator: String)
}

interface UIElementLocators

/**
 * 剪映桌面版UI元素定位器
 * @property clipWindow 剪辑窗口
 * @author dongjak
 * @created 2024/10/09
 * @version 1.0
 * @since 1.0
 */
interface JianyingDesktopUIElementLocators : UIElementLocators {
    val clipWindow: String
}

/**
 * 剪映桌面版客户端服务
 *
 * @property executablePath 可执行文件路径
 * @property clickniumService `clicknium`ui自动化服务
 * @property draftDir 草稿根目录
 * @property version 版本号
 * @author dongjak
 * @created 2024/10/09
 * @version 1.0
 * @since 1.0
 */
class JianyingDesktopService(
    private val executablePath: String,
    private val clickniumService: ClickniumService,
    private val locators: JianyingDesktopUIElementLocators = object : JianyingDesktopUIElementLocators {
        override val clipWindow: String
            get() = "locator.jianyingpro.剪辑窗口"
    }
) {
    val version by lazy {
        val commandLine = executablePath.asCommandLine()
        /*val executor = DefaultExecutor.Builder()
            .get()
        val resultHandler = DefaultExecuteResultHandler()

        val watchdog = ExecuteWatchdog.Builder()
            .setTimeout(Timeouts.minutes(1).duration)
            .get()
        executor.watchdog = watchdog
        try {
            // 异步执行命令
            executor.execute(commandLine, resultHandler)

            // 等待一段时间
            Thread.sleep(5000) // 等待5秒

            // watchdog.destroyProcess()
            // 获取进程PID
            val pid = executor.getProcessId()
            println("Process PID: $pid")

            // 使用taskkill命令强制终止进程及其所有子进程
            pid.kill()
            // 等待进程完全终止
            resultHandler.waitFor()

            println("Exit value: ${resultHandler.exitValue}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error: ${e.message}")
        }*/
    }

    val draftDir by lazy {
        File("")
    }

}
