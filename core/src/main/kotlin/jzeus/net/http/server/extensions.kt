package jzeus.net.http.server

import io.javalin.http.Context

fun <T> Context.jsonBody(clazz: Class<T>): T {
    return this.bodyAsClass(clazz)
}
