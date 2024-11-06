package jzeus.media.audio

import cn.hutool.core.io.unit.DataSize
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExtensionsKtTest {

    @Test
    fun resize() {
        val newFile = "C:\\Users\\cruld\\Desktop\\original.mp3".toAudioFile().resize(DataSize.ofMegabytes(10))
        assertTrue(newFile.size <= DataSize.ofMegabytes(10))


    }
}
