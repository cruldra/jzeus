package jzeus.result

import org.slf4j.Logger
import org.slf4j.event.Level

// 运算符重载
infix fun <T, E> Result<T, E>.or(default: T): T = when (this) {
    is Result.Ok -> value
    is Result.Err -> default
}

// 链式调用支持
fun <T, E, R> Result<T, E>.andThen(transform: (T) -> Result<R, E>): Result<R, E> =
    when (this) {
        is Result.Ok -> transform(value)
        is Result.Err -> Result.Err(error)
    }

// 类似Rust的？运算符
suspend fun <T, E> Result<T, E>.onError(block: suspend (E) -> Unit): Result<T, E> =
    apply { if (this is Result.Err) block(error) }


fun <V> resultOf(block: () -> V): Result<V, Throwable> = try {
    Result.Ok(block())
} catch (e: Throwable) {
    Result.Err(e)
}

fun <T, E> Result<T, E>.handle(block: ResultHandler<T, E>.() -> Unit) {
    ResultHandler(this).block()
}

fun <T> kotlin.Result<T>.log(
    logger: Logger,
    successLevel: Level = Level.INFO,
    errorLevel: Level = Level.ERROR,
    message: (T) -> String
): kotlin.Result<T> {
    this.onSuccess { value ->
        when (successLevel) {
            Level.INFO -> if (logger.isInfoEnabled) logger.info(message(value))
            Level.WARN -> logger.warn(message(value))
            Level.ERROR -> logger.error(message(value))
            Level.DEBUG -> logger.debug(message(value))
            Level.TRACE -> logger.trace(message(value))
            else -> logger.info(message(value))
        }
    }

    this.onFailure { error ->
        when (errorLevel) {
            Level.INFO -> if (logger.isInfoEnabled) logger.info(error.message)
            Level.WARN -> logger.warn(error.message)
            Level.ERROR -> logger.error(error.message)
            Level.DEBUG -> logger.debug(error.message)
            Level.TRACE -> logger.trace(error.message)
            else -> logger.error(error.message)
        }
    }

    return this
}
