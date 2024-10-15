package jzeus.cron

import it.sauronsoftware.cron4j.Scheduler

fun cron(expression: String, block: () -> Unit) {
    val s = Scheduler()
    s.schedule(expression, block)
    s.start()
}
