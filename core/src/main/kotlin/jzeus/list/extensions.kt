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

fun <E> List<E>.skip(skip:Int):List<E> {
    return if (skip >= size) {
        emptyList()
    } else {
        this.subList(skip, size)
    }
}
