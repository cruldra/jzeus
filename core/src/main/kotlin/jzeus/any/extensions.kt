package jzeus.any


/**
 * 将任意值转换为布尔值,类似于`Python`中的[`bool()`](https://poe.com/s/hsftI6p1jZZgI6wYY2nw)函数
 */
fun Any?.truthValue(): Boolean {
    return when (this) {
        null -> false
        is Boolean -> this
        is Number -> this != 0 && this != 0.0
        is String -> this.isNotEmpty() && this != "null" && this != "n" && this != "false" && this != "no"
        is Collection<*> -> this.isNotEmpty()
        is Map<*, *> -> this.isNotEmpty()
        is Array<*> -> this.isNotEmpty()
        else -> true
    }
}
