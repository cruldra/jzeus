package jzeus.validation

import jakarta.validation.Configuration
import jakarta.validation.Validation
import jakarta.validation.Validator
import jzeus.failure.failure

fun validator(block: Configuration<*>.() -> Unit = {}): Validator {
    val configuration: Configuration<*> = Validation.byDefaultProvider()
        .configure()
    configuration.block()
    return configuration.buildValidatorFactory().validator
}

fun <T> T.validate(): T = validator {
    addProperty("jakarta.validation.fail_fast", "true")
}.validate(
    this
).firstOrNull()?.message?.let {
    failure<T>(it, 0)
} ?: this
