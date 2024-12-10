package jzeus.crawler


data class ChromeProperties(
    val executable: String,
    val userDataDir: String,
    val cdpPort: Int = 8654,
    val headless: Boolean = false,
    val windowWidth: Int = 1920,
    val windowHeight: Int = 1080,
    val userAgent: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36",
)
