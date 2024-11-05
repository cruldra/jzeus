package jzeus.jianying

import jzeus.datetime.Timeout
import jzeus.datetime.Timeouts
import jzeus.failure.failure
import jzeus.file.raiseForNotExists
import jzeus.file.siblingFile
import jzeus.json.objectMapper
import jzeus.list.raiseForSizeLessThan
import jzeus.log.LoggerDelegate
import jzeus.os.exec
import jzeus.process.kill
import jzeus.process.pids
import jzeus.process.toProcessName
import jzeus.str.asCommandLine
import jzeus.task.retry
import jzeus.task.wait
import jzeus.win32.WindowState
import jzeus.win32.setWindowState
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.File


interface ClickniumService {

    /**
     * 点击元素
     */
    @POST("/click_ui_element")
    fun click(@Body payloads: ClickUiElementRequest)


    /**
     * 将鼠标悬停在元素上
     */
    @POST("/hover_ui_element")
    fun hover(@Body payloads: ClickUiElementRequest)


    /**
     * 判断元素是否存在
     * @return 是否存在
     */
    @POST("/ui_element_exists")
    fun exists(@Body payloads: ClickUiElementRequest): Boolean


    /**
     * 输入文本
     */
    @POST("/type_text_into_ui_element")
    fun typeText(@Body payloads: TypeTextRequest)


    /**
     * 设置`clicknium`的授权码
     */
    @POST("/set_clicknium_license")
    fun setLicense(@Body payloads: SetClickniumLicenseRequest)


    /**
     * 按下键盘按键
     */
    @POST("/key_down")
    fun keyDown(@Body payloads: KeyRequest)


    /**
     * 释放键盘按键
     */
    @POST("/key_up")
    fun keyUp(@Body payloads: KeyRequest)

    /**
     * 激活窗口
     */
    @Deprecated(
        "不太稳定,用jzeus.win32.ExtensionsKt.setWindowState代替"
    )
    @POST("/set_clicknium_license")
    fun activateWindow(@Body payloads: ClickUiElementRequest)


    /**
     * 窗口最大化
     */
    @POST("/window_maximize")
    fun windowMaximize(@Body payloads: WindowMaximizeRequest)

    /**
     * 点击屏幕上的图片
     */
    @POST("/click_img")
    fun clickImg(@Body payloads: ClickImageRequest)
}

interface UIElementLocators

/**
 * 剪映桌面版UI元素定位器
 * @property clipWindow 剪辑窗口
 * @property draftListFirstElement 草稿列表第一个元素
 * @property draftSearchBox 草稿搜索框
 * @property draftSearchButton 草稿搜索按钮
 * @property mainWindow 剪映主窗口
 * @property clipWindowTextSegment 剪辑窗口中的文本片段
 * @property clipWindowMaxBtn 剪辑窗口最大化按钮
 * @property clipWindowDigitalHumanTab 剪辑窗口上的数字人标签页
 * @property clipWindowDigitalHumanListFirst 剪辑窗口上的数字人列表中的第一个元素
 * @property clipWindowAddDigitalHumanBtn 剪辑窗口上的添加数字人按钮
 * @property clipWindowVideoTrack 剪辑窗口上的视频轨道
 * @property clipWindowChangeSoundTab 剪辑窗口上的更换音色标签页
 * @property clipWindowSoundListFirst 剪辑窗口上的音色列表中的第一个元素
 * @property clipWindowAddSoundBtn 剪辑窗口上的添加音色按钮
 * @property closeUpdateWindowBtn 用于关闭更新窗口的按钮
 * @property closeDraftListErrorDialogBtn 用于关闭草稿列表错误对话框的按钮
 * @author dongjak
 * @created 2024/10/09
 * @version 1.0
 * @since 1.0
 */
interface JianyingDesktopUIElementLocators : UIElementLocators {
    val mainWindow: String
    val clipWindow: String
    val clipWindowTextSegment: String
    val clipWindowMaxBtn: String
    val clipWindowDigitalHumanTab: String
    val clipWindowAddDigitalHumanBtn: String
    val clipWindowDigitalHumanListFirst: String
    val clipWindowSoundListFirst: String
    val clipWindowAddSoundBtn: String
    val clipWindowChangeSoundTab: String
    val clipWindowVideoTrack: String
    val draftListFirstElement: String
    val draftSearchBox: String
    val draftSearchButton: String
    val closeUpdateWindowBtn: String
    val closeDraftListErrorDialogBtn: String
}

open class DefaultJianyingDesktopUIElementLocators : JianyingDesktopUIElementLocators {
    override val mainWindow: String = "locator.jianyingpro.剪映主窗口"
    override val clipWindow: String = "locator.jianyingpro.剪辑窗口"
    override val clipWindowTextSegment: String = "locator.jianyingpro.文本片段"
    override val draftListFirstElement: String = "locator.jianyingpro.草稿列表中的第一个元素"
    override val draftSearchBox: String = "locator.jianyingpro.草稿搜索框"
    override var draftSearchButton: String = "locator.jianyingpro.草稿搜索按钮"
    override val closeUpdateWindowBtn: String = "locator.jianyingpro.关闭版本更新窗口"
    override val closeDraftListErrorDialogBtn: String = "locator.jianyingpro.草稿列表异常窗口上的取消按钮"
    override val clipWindowMaxBtn: String = "locator.jianyingpro.剪辑窗口最大化按钮"
    override val clipWindowDigitalHumanTab: String = ".locator/pyautogui/jianyingpro_img/1.png"
    override val clipWindowAddDigitalHumanBtn: String = ".locator/pyautogui/jianyingpro_img/generate.png"
    override val clipWindowDigitalHumanListFirst: String = "locator.jianyingpro.数字人"
    override val clipWindowSoundListFirst: String = "locator.jianyingpro.音色"
    override val clipWindowAddSoundBtn: String = ".locator/pyautogui/jianyingpro_img/Start reading.png"
    override val clipWindowChangeSoundTab: String = ".locator/pyautogui/jianyingpro_img/change_sound_tab2.png"
    override val clipWindowVideoTrack: String = "locator.jianyingpro.视频轨道"
}

/**
 * 剪映桌面版客户端服务
 *
 * @property executablePath 可执行文件路径
 * @property clickniumService `clicknium`ui自动化服务
 * @author dongjak
 * @created 2024/10/09
 * @version 1.0
 * @since 1.0
 */
class JianyingDesktop(
    private val executablePath: String,
    private val clickniumService: ClickniumService,
    private val locators: JianyingDesktopUIElementLocators = DefaultJianyingDesktopUIElementLocators(),
    private val defaultTimeout: Timeout = Timeouts.minutes(1)
) {
    private val log by LoggerDelegate()

    companion object {
        private val PROCESS_NAME = "JianyingPro".toProcessName()
    }

    private var draft: Draft.Draft? = null
    private fun raiseForDraftNull() {
        if (draft == null) failure<Any>("当前没有打开草稿")
    }

    /**
     * 启动剪映桌面版客户端
     *
     * @return 是否启动成功
     */
    fun start(): Boolean {
        executablePath.asCommandLine().exec()
        val res = isRunning()
        if (res) {
            //clickniumService.activateWindow(locators.mainWindow, timeout =180 )
            setWindowState(PROCESS_NAME.value, WindowState.SHOW)

            runCatching {
                clickniumService.click(
                    ClickUiElementRequest(
                        locators = locators.closeUpdateWindowBtn, timeout = 2
                    )
                )
            }
            runCatching {
                clickniumService.click(
                    ClickUiElementRequest(
                        locators = locators.closeDraftListErrorDialogBtn, timeout = 2
                    )
                )
            }
        }
        return res
    }

    private fun isRunning(): Boolean {

        return retry(15, 2) {
            val pids = PROCESS_NAME.pids
            pids.raiseForSizeLessThan(4, "剪映未完全启动")
            pids.size >= 4
        } ?: false
    }

    /**
     * 关闭剪映桌面版客户端
     * @return 是否关闭成功
     */
    fun stop(): Boolean {
        val res = PROCESS_NAME.pids.all {
            it.kill()
        } or PROCESS_NAME.pids.isEmpty()
        return res
    }

    private fun raiseForNotRunning() {
        if (!isRunning()) failure<Any>("剪映未运行")
    }

    private fun selectTextSegments(range: IntRange) {
        range.forEach { index ->
            //如果是第一个文本片段，需要先hover一下，然后按下ctrl键
            if (index == range.first) {
                clickniumService.hover(
                    ClickUiElementRequest(
                        locators = locators.clipWindowTextSegment, index = index
                    )
                )

                clickniumService.keyDown(KeyRequest("ctrl"))
            }
            clickniumService.click(
                ClickUiElementRequest(
                    locators = locators.clipWindowTextSegment, index = index
                )
            )
        }
        clickniumService.keyUp(KeyRequest("ctrl"))
    }

    fun openDraft(draft: Draft.Draft) {
        this.draft = draft
        raiseForNotRunning()
        //如果搜索框不可见,则先点击搜索按钮

        if (!clickniumService.exists(
                ClickUiElementRequest(
                    locators = locators.draftSearchBox,
                )
            )
        ) clickniumService.click(
            ClickUiElementRequest(
                locators = locators.draftSearchButton,
            )
        )
        //输入草稿名称
        clickniumService.typeText(
            TypeTextRequest(
                locators = locators.draftSearchBox,
                text = draft.name,
            )
        )
        //在最多10秒内等待草稿列表中的第一个元素出现

        val res = wait(10, Timeouts.ONE_SECOND) {
            if (clickniumService.exists(
                    ClickUiElementRequest(
                        locators = locators.draftListFirstElement,
                    )
                )
            ) true
            else null
        } ?: failure("草稿打开失败,因为没有定位到草稿元素")
        //然后点击草稿列表中的第一个元素

        clickniumService.click(
            ClickUiElementRequest(
                locators = locators.draftListFirstElement,
            )
        )
        //最后在最多10秒内等待剪辑窗口出现
        wait(10, Timeouts.ONE_SECOND) {

            if (clickniumService.exists(
                    ClickUiElementRequest(
                        locators = locators.clipWindow,
                    )
                )
            ) true
            else null
        } ?: failure("草稿打开失败,因为剪辑窗口未打开")
        //最大化剪辑窗口

        clickniumService.windowMaximize(
            WindowMaximizeRequest(
                locators = locators.clipWindow,
                maxBtnLocator = locators.clipWindowMaxBtn
            )
        )
    }

    /**
     * 添加数字人
     * @param digitalHumanIndex 数字人索引
     * @param audioIndex 音色索引
     * @param timeout 超时时间,默认`10`分钟
     * @return 数字人视频文件
     */
    fun addDigitalHuman(
        digitalHumanIndex: Int = 2, audioIndex: Int = 1, timeout: Timeout = Timeouts.minutes(10)
    ): File {
        //必须先打开草稿
        raiseForDraftNull()
        //选中所有文本片段
        selectTextSegments(1..this.draft!!.content.tracks.filter {
            it.type == "text"
        }.flatMap {
            it.segments
        }.size)
        //3秒后点击右上方"数字人"标签页

        clickniumService.sleep(3).clickImg(
            ClickImageRequest(
                imgPath = locators.clipWindowDigitalHumanTab
            )
        )
        //在最多5秒内等待数字人列表出现
        wait(5, Timeouts.ONE_SECOND) {
            if (clickniumService.exists(
                    ClickUiElementRequest(
                        locators = locators.clipWindowDigitalHumanListFirst
                    )
                )
            ) true
            else null
        } ?: failure("数字人视频生成失败,因为数字人列表未出现")
        //然后点击

        clickniumService.click(
            ClickUiElementRequest(
                locators = locators.clipWindowDigitalHumanListFirst,
                index = digitalHumanIndex
            )
        )
        //3秒后点击"添加数字人"按钮

        clickniumService.sleep(3).clickImg(
            ClickImageRequest(
                imgPath = locators.clipWindowAddDigitalHumanBtn
            )
        )
        //在最多30秒内等待视频轨道出现
        wait(30, Timeouts.ONE_SECOND) {
            if (clickniumService.exists(
                    ClickUiElementRequest(
                        locators = locators.clipWindowVideoTrack,
                    )
                )
            ) true
            else null
        } ?: failure("数字人视频生成失败,选择数字人模板时出错")
        //选中视频轨道

        clickniumService.click(
            ClickUiElementRequest(
                locators = locators.clipWindowVideoTrack,
            )
        )
        //3秒后点击右上方"更换音色"标签页

        clickniumService.sleep(3).clickImg(
            ClickImageRequest(
                imgPath = locators.clipWindowChangeSoundTab
            )
        )
        //在最多5秒内等待音色列表出现
        wait(5, Timeouts.ONE_SECOND) {

            if (clickniumService.exists(
                    ClickUiElementRequest(
                        locators = locators.clipWindowSoundListFirst,
                    )
                )
            ) true
            else null
        } ?: failure("数字人视频生成失败,选择音色时出错")
        //然后点击音色

        clickniumService.click(
            ClickUiElementRequest(
                locators = locators.clipWindowSoundListFirst,
                index = audioIndex
            )
        )
        //3秒后点击"添加音色"按钮

        clickniumService.sleep(3).clickImg(
            ClickImageRequest(
                imgPath = locators.clipWindowAddSoundBtn
            )
        )
        //最后在指定超时时间内等待视频文件生成
        val file = wait(timeout.duration.seconds.toInt(), Timeouts.ONE_SECOND) {
            try {
                val contentJsonFile = this.draft?.files?.contentJsonFile ?: failure("草稿文件错误")
                val taskId = objectMapper.readValue(
                    contentJsonFile, Draft.Content::class.java
                ).materials.digitalHumans[0].localTaskId
                contentJsonFile.siblingFile("/Resources/digitalHuman/${taskId}.mp4").raiseForNotExists()
            } catch (e: Exception) {
                log.warn(e.message)
                null
            }
        } ?: failure("视频生成失败,因为视频文件未生成")
        return file
    }
}
