package jzeus.retry

import jzeus.cls.isSub
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * 在[任务][task]执行过程中抛出[指定异常][extension]时重试[指定的次数][count]
 * @param task  Function1 任务
 * @param count Int 重试次数
 * @param interval Duration 重试间隔
 * @param exception  Exception 在任务抛出此异常时才会重试
 * @return V
 */
fun <V> retry(
    count: Int,
    interval: Duration,
    exception: Class<out Exception> = Exception::class.java,
    task: (index: Int) -> V
): V? {

    var res: V? = null
    loop@ for (i in 1..count) {
        try {
            res = task(i)
            break@loop
        } catch (e: Throwable) {
            if (e::class.java.isSub(exception)) {
                if (i == count) throw  e
            } else throw  e
        }
        TimeUnit.SECONDS.sleep(interval.toSeconds())
    }
    return res
}
