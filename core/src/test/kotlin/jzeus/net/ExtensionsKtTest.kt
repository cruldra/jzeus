package jzeus.net

import jzeus.file.toFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExtensionsKtTest {


    @Test
    fun test1() {
          "https://www.douyin.com/aweme/v1/play/?aid=6383&file_id=1864df0a94784c7495efa8787f3cc5aa&is_play_url=1&is_ssr=1&line=0&sign=88762d0fa24d20051e3fc4e44330d24c&source=PackSourceEnum_AWEME_DETAIL&uifid=60b2ef133e5e740633c50bb923c1ddfcacd13dfeee1bbba287269d01840b457b6447e7d64b96f73cf44b420d305f33fa17a692a80fda33988f55eae666e2c8d3a08fdc4212043f1d28bff71c06b44c10efe7e29e161dc869bb9a3acd2c34b9920e5024b124ec9a04257bb09a67106fd18c5846730f0e7e023987cbbe032b920e8b08776efbfefc67cc0a2f3b0d9d402b446a4a78998553c88c0dee75d3392880&video_id=v0200fg10000crbg5uvog65jj0e92p5g".toURL()
            .copyToFile("C:\\Users\\cruld\\Downloads\\a.mp4".toFile())
    }
}
