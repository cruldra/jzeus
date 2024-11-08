package jzeus.result

sealed class Result<out T, out E> {
    data class Ok<out T>(val value: T) : Result<T, Nothing>()
    data class Err<out E>(val error: E) : Result<Nothing, E>()

    fun isOk() = this is Ok
    fun isErr() = this is Err

    // 类似Rust的unwrap
    fun unwrap(): T = when (this) {
        is Ok -> value
        is Err -> throw RuntimeException("Called unwrap on an Err value")
    }

    // 类似Rust的unwrapOr
    fun unwrapOr(default: @UnsafeVariance T): T = when (this) {
        is Ok -> value
        is Err -> default
    }

    // 类似Rust的map
    fun <R> map(transform: (T) -> R): Result<R, E> = when (this) {
        is Ok -> Ok(transform(value))
        is Err -> Err(error)
    }

    // 类似Rust的mapErr
    fun <F> mapErr(transform: (E) -> F): Result<T, F> = when (this) {
        is Ok -> Ok(value)
        is Err -> Err(transform(error))
    }
}
class ResultHandler<T, E>(private val result: Result<T, E>) {
    infix fun success(block: (T) -> Unit) {
        if (result is Result.Ok) block(result.value)
    }

    infix fun error(block: (E) -> Unit) {
        if (result is Result.Err) block(result.error)
    }
}
