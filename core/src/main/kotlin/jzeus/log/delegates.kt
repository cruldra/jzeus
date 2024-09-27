package jzeus.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * [SLF4J loggers in 3 ways][https://www.reddit.com/r/Kotlin/comments/8gbiul/slf4j_loggers_in_3_ways/]
 */
class LoggerDelegate : ReadOnlyProperty<Any?, Logger> {

    companion object {
        private fun <T> createLogger(clazz: Class<T>): Logger {
            return LoggerFactory.getLogger(clazz)
        }
    }

    private var logger: Logger? = null

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        if (logger == null) {
            logger = createLogger(thisRef?.javaClass ?: Any::class.java)
        }
        return logger!!
    }
}
