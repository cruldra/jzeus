package jzeus.jianying

import jzeus.os.exec
import jzeus.process.kill
import jzeus.process.pids
import jzeus.process.toProcessName
import jzeus.str.asCommandLine
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
    private val draftDir: File,
    private val clickniumService: ClickniumService,
    private val locators: JianyingDesktopUIElementLocators = object : JianyingDesktopUIElementLocators {
        override val clipWindow: String
            get() = "locator.jianyingpro.剪辑窗口"
    }
) {

    companion object {
        private val PROCESS_NAME = "JianyingPro.exe".toProcessName()
    }

    /**
     * 启动剪映桌面版客户端
     *
     * @return 是否启动成功
     */
    fun start(): Boolean {
        executablePath.asCommandLine().exec()
        return PROCESS_NAME.pids.isNotEmpty()
    }

    /**
     * 关闭剪映桌面版客户端
     * @return 是否关闭成功
     */
    fun stop(): Boolean {
        val res = PROCESS_NAME.pids.all {
            it.kill()
        }
        return res or PROCESS_NAME.pids.isEmpty()
    }
}
