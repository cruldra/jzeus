package jzeus.win32

import jzeus.console.printToConsole
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class ExtensionsKtTest {

    @Test
    fun testSetWindowState() {

      setWindowState("JianyingPro", WindowState.SHOW)
    }

    @Test
    fun test2(){
        //$2a$10$nfiquwCxDBcxS3G3eeYPvOidC.drIi53tp5CvceWubmt4iAWCluSq
        //$2a$10$4VldlDtbpfI2n2rM7YRbSOjh0s/O0fVA7OK5MnZCUNEbm4VNmuucG
//        BCryptPasswordEncoder().encode("123394").printToConsole()
        BCryptPasswordEncoder().matches("123394","""${'$'}2a${'$'}10${'$'}6U6B2tG/WSRrsvJhP1lyf.ZGcpzBvXdWWbBTv9FMuB.jkqHzLqO7G""")
            .printToConsole()
    }
}
