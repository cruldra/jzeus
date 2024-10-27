package jzeus.process

import jzeus.console.printToConsole
import org.junit.jupiter.api.Test

class ExtensionsKtTest {

    @Test
    fun testGetPids() {
        "JianyingPro".toProcessName().pids.printToConsole()
    }
}
