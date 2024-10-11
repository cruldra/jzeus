package jzeus.crawler

import com.google.gson.Gson
import com.microsoft.playwright.*

/**
 * 创建一个`playwright`实例并使用
 *
 * ## 示例
 * ```kotlin
 * playwright {
 *     chromeOverCDP(cdpUrl) {
 *
 *     }
 * }
 * ```
 *
 * @param block 浏览器操作代码块
 * @return 由[浏览器操作代码块][block]返回的结果
 */
fun <R> playwright(block: Playwright.() -> R): R {
    return Playwright.create().use {
        block(it)
    }
}

/**
 * 通过`cdp`连接到`chrome`浏览器
 *
 * ## 示例
 *
 * ```kotlin
 *  chromeOverCDP(cdpUrl) {
 *
 * }
 * ```
 *
 *
 * @param cdpUrl cdp地址
 * @param timeout 超时时间
 * @param block 浏览器操作代码块
 * @return 由[浏览器操作代码块][block]返回的结果
 */
fun <R> Playwright.chromeOverCDP(cdpUrl: String, timeout: Int = 3, block: Browser.() -> R): R {
    val browser = chromium().connectOverCDP(cdpUrl, connectOverCdpOptions {
        setTimeout((timeout * 1000).toDouble())
    })
    return browser.use(block)
}

fun connectOverCdpOptions(block: BrowserType.ConnectOverCDPOptions.() -> Unit): BrowserType.ConnectOverCDPOptions {
    return BrowserType.ConnectOverCDPOptions().apply(block)
}

fun pageWaitForSelectorOptions(block: Page.WaitForSelectorOptions.() -> Unit): Page.WaitForSelectorOptions {
    return Page.WaitForSelectorOptions().apply(block)
}

fun elementWaitForSelectorOptions(block: ElementHandle.WaitForSelectorOptions.() -> Unit): ElementHandle.WaitForSelectorOptions {
    return ElementHandle.WaitForSelectorOptions().apply(block)
}


fun Page.WaitForSelectorOptions.timeoutOfSeconds(seconds: Int): Page.WaitForSelectorOptions {
    return setTimeout(seconds * 1000.toDouble())
}

fun Page.waitForURLOptions(block: Page.WaitForURLOptions.() -> Unit): Page.WaitForURLOptions {
    return Page.WaitForURLOptions().apply(block)
}

fun Page.WaitForURLOptions.timeoutOfSeconds(seconds: Int): Page.WaitForURLOptions {
    return setTimeout(seconds * 1000.toDouble())
}

fun Page.WaitForURLOptions.timeoutOfMinutes(minutes: Int): Page.WaitForURLOptions {
    return setTimeout(minutes * 60 * 1000.toDouble())
}

/**
 * 打开新页面
 *
 * ## 示例
 *
 * ```kotlin
 * playwright {
 *     chromeOverCDP(cdpUrl) {
 *         page(params.url,listOf(AntiCrawlerDetectionPlugin())){
 *
 *         }
 *     }
 * }
 * ```
 *
 * @param url 页面地址
 * @param plugins 插件
 * @param block 页面操作
 */
fun <R> Browser.page(
    url: String,
    plugins: List<Plugin> = emptyList(),
    block: Page.() -> R
): R {
    val page = contexts().first().newPage()
    return page.use {
        plugins.onEach { plugin ->
            plugin.apply(page)
        }
        page.navigate(url)
        block(page)
    }
}

/**
 * 滚动到页面底部
 */
fun Page.scrollToBottom() {
    evaluate("window.scrollTo(0, document.body.scrollHeight)")
}


/**
 * 在页面上打开一个新的`cdp`会话
 *
 * ## 示例
 *
 * ```kotlin
 * cdpSession {
 *     send("Network.enable")
 *     send(
 *         "Network.setCacheDisabled", mapOf(
 *             "cacheDisabled" to true
 *         )
 *     )
 *     on("Network.requestWillBeSent") { request ->
 *         runCatching {
 *             val requestUrl = request.getAsJsonObject("request").get("url").asString.toURL()
 *             if (requestUrl.getQueryParameter("mime_type") == "video_mp4") {
 *                 hoveredVideo.downloadUrl = requestUrl.toString()
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param block 会话操作
 */
fun <R> Page.cdpSession(block: CDPSession.() -> R): R {
    val cdpSession = context().newCDPSession(this)
    return block(cdpSession)
}

fun CDPSession.send(method: String, params: Map<String, Any>): Any? {
    val gson = Gson()
    val jsonObject = gson.toJsonTree(params).asJsonObject
    return send(method, jsonObject)
}


val ElementHandle.parent: ElementHandle
    get() = this.evaluateHandle("(element) => element.parentElement") as ElementHandle
val ElementHandle.outerHTML: String
    get() = this.evaluate("(element) => element.outerHTML") as String

fun ElementHandle.sleep(seconds: Int): ElementHandle {
    jzeus.async.sleep(seconds.toLong())
    return this
}
/*
fun ElementHandle.clickCenter(){
    val box = this.boundingBox()
    //mouse().click(box.x + box.width / 2, box.y + box.height / 2)//在视频卡片中间点击进入视频详情页
}
*/
