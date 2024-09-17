package jzeus.process

fun Collection<ProcessHandle>.killAll() {
    forEach { it.destroy() }
}
