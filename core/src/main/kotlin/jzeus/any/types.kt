package jzeus.any

data class Range<T : Comparable<T>>(val min: T, val max: T) {
    init {
        require(min <= max) { "Minimum value must be less than or equal to maximum value" }
    }

    operator fun contains(value: T): Boolean = value in min..max
}

data class Offset(
    val x: Float = 0.0f,
    val y: Float = 0.0f
)

data class Size(
    val width: Int = 0,
    val height: Int = 0
)

object Sizes {

    val Douyin = Size(1080, 1920)
}
