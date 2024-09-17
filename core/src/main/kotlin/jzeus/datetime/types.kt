package jzeus.datetime

import java.time.Duration

class Timeout(private val milliseconds: Long) {


    val duration: Duration
        get() = Duration.ofMillis(milliseconds)


}

object Timeouts {
    val NEVER = Timeout(Long.MAX_VALUE)
}
