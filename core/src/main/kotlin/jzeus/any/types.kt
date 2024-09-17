package jzeus.any

data class Range<T : Comparable<T>>(val min: T, val max: T) {
    init {
        require(min <= max) { "Minimum value must be less than or equal to maximum value" }
    }

    operator fun contains(value: T): Boolean = value in min..max
}
