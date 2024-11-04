package jzeus.log

import org.slf4j.Logger
import org.slf4j.event.Level

fun <T> T.log(logger: Logger, level: Level = Level.INFO, message: (T) -> String): T {
    when (level) {
        Level.INFO -> logger.info(message(this))
        Level.WARN -> logger.warn(message(this))
        Level.ERROR -> logger.error(message(this))
        Level.DEBUG -> logger.debug(message(this))
        Level.TRACE -> logger.trace(message(this))
        else -> logger.info(message(this))
    }
    return this
}
