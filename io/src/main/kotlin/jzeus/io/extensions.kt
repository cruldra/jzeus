package jzeus.io

import java.io.File


fun String.asFile(): File = File(this)
fun File.createIfNotExists(): File = if (exists()) this else apply { createNewFile() }
