package jzeus.net.http.server

import org.springframework.core.annotation.AliasFor


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PostMapping(
    vararg val value: String = [],
    val name: String = "",
    val path: Array<String> = [],
    val params: Array<String> = [],
    val headers: Array<String> = [],
    val consumes: Array<String> = [],
    val produces: Array<String> = []
)

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class GetMapping(
    vararg val value: String = [],
    val name: String = "",
    val path: Array<String> = [],
    val params: Array<String> = [],
    val headers: Array<String> = [],
    val consumes: Array<String> = [],
    val produces: Array<String> = []
)

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DeleteMapping(
    vararg val value: String = [],
    val name: String = "",
    val path: Array<String> = [],
    val params: Array<String> = [],
    val headers: Array<String> = [],
    val consumes: Array<String> = [],
    val produces: Array<String> = []
)
