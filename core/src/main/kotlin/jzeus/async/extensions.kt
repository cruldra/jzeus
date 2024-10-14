package jzeus.async

import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun sleep(seconds: Long) = TimeUnit.SECONDS.sleep(seconds)
fun sleepRandom(min: Long = 1, max: Long = 10) = TimeUnit.SECONDS.sleep(Random.nextLong(min, max))
