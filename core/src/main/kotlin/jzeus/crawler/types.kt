package jzeus.crawler

import jzeus.net.availablePort


data class ChromeProperties(
    val executable: String,
    val userDataDir: String,
    val cdpPort: Int = availablePort(),
    val headless: Boolean = false,
    val windowWidth: Int = 1680,
    val windowHeight: Int = 900,
    val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36",
)
