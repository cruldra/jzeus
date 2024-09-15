package jzeus.bool

import jzeus.failure.failure

/**
 * 如果这个布尔值不是`true`,则抛出[异常][jzeus.failure.Failure]
 *
 * @param message 异常消息
 * @param code 异常代码
 */
fun Boolean.raiseIfNotTrue(message: String, code: Int = 0) = if (this) this else failure(message, code)

/**
 * 如果这个布尔值不是`false`,则抛出[异常][jzeus.failure.Failure]
 * @param message 异常消息
 * @param code 异常代码
 */
fun Boolean.raiseIfTrue(message: String, code: Int = 0) = if (!this) this else failure(message, code)
