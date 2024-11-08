package jzeus.result

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
