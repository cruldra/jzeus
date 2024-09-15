package jzeus.uuid

fun uuid() = java.util.UUID.randomUUID().toString().replace("-", "")
