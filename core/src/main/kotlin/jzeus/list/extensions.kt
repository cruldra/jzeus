package jzeus.list

/**
 * 获取列表的前`n`个元素,如果`n`大于列表长度,则返回整个列表
 */
fun <E> List<E>.subList(length: Int): List<E> {
    return if (length <= size) {
        this.subList(0, length)
    } else {
        this
    }
}

fun <E> List<E>.skip(skip: Int): List<E> {
    return if (skip >= size) {
        emptyList()
    } else {
        this.subList(skip, size)
    }
}


fun <E> List<E>.asMutableList(): MutableList<E> {
    return this as MutableList<E>
}

/**
 * 找到第一个符合条件的元素,如果找不到则返回默认值
 *
 * ```kotlin
 * val list = listOf(1, 2, 3, 4, 5)
 * val firstEven = list.firstOrDefault(0) { it % 2 == 0 }
 * println(firstEven) // 输出 2
 * val firstOdd = list.firstOrDefault(0) { it == 0 }
 * println(firstOdd) // 输出 0
 * ```
 *
 * @param default 默认值
 * @param filter 过滤器
 * @return 第一个符合条件的元素,如果找不到则返回默认值
 */
fun <E> List<E>.firstOrDefault(filter: (E) -> Boolean, default: E): E {
    return this.firstOrNull(filter) ?: default
}

fun <E> List<E>.firstOrGet(filter: (E) -> Boolean, getter: List<E>. () -> E): E {
    return this.firstOrNull(filter) ?: getter()
}


/**
 * 将元素添加到列表中并返回该元素
 */
fun <E> MutableList<E>.addAndGet(element: E): E {
    this.add(element)
    return element
}

fun <E> List<E>.add(element: E): Boolean {
    return (this as MutableList<E>).add(element)
}

/**
 * 如果列表为空,则抛出异常
 * @param message 异常信息
 */
fun <E> Collection<E>?.raiseForEmpty(message: String): Collection<E> {
    if (this.isNullOrEmpty()) {
        throw NoSuchElementException(message)
    }
    return this
}
