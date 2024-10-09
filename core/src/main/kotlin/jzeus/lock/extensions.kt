package jzeus.lock

import jzeus.async.sleep
import jzeus.datetime.Timeout

fun <R> DatabaseLock.use(lockKey: String, timeout: Timeout, block: () -> R): R {
    while (!acquireLock(lockKey, timeout)) {
        sleep(1)
    }
    try {
        return block()
    } finally {
        releaseLock(lockKey)
    }
}
