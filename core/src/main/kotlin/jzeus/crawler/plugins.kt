package jzeus.crawler

import com.microsoft.playwright.Page

interface Plugin {
    fun apply(page: Page)
}

/**
 * 这个插件用于在页面上启用反爬虫检测
 * @author dongjak
 * @created 2024/09/16
 * @version 1.0
 * @since 1.0
 */
class AntiCrawlerDetectionPlugin : Plugin {
    override fun apply(page: Page) {
        page.addInitScript("https://ailoveworld.oss-cn-hangzhou.aliyuncs.com/stealth.min.js")
    }
}
