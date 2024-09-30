package jzeus.enum

import java.util.*

fun Enum<*>.toKebabCase() = this.name.lowercase(Locale.getDefault()).replace("_", "-")
