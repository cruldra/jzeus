package jzeus.net

import org.apache.commons.exec.OS
import java.net.Proxy

data class Proxies(
    val http: Proxy? = null,
    val https: Proxy? = null,
    val socks: Proxy? = null,
)

fun main() {
    println(OS.isFamilyWindows())
}
