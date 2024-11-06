package jzeus.primitive

import kotlin.math.ceil
import kotlin.math.floor

/**
 * 对这个浮点数进行向上取整
 *
 * ## 示例
 *
 * ```kotlin
 * val number = 3.14
 * val roundedNumber = number.ceil()
 * println(roundedNumber) // 输出: 4.0
 * ```
 *
 * @author dongjak
 * @created 2024/11/06
 * @version 1.0
 * @since 1.0
 */
fun Double.ceil() = ceil(this)


/**
 * 对这个浮点数进行向下取整
 *
 * ## 示例
 *
 * ```kotlin
 * val number = 3.14
 * val roundedNumber = number.floor()
 * println(roundedNumber) // 输出: 3.0
 * ```
 *
 * @author dongjak
 * @created 2024/11/06
 * @version 1.0
 * @since 1.0
 */
fun Double.floor() = floor(this)
