package jzeus.list

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
