package jzeus.task

import cn.hutool.core.thread.ThreadFactoryBuilder
import jzeus.cls.isSub
import java.time.Duration
import java.util.concurrent.*

/**
 * 在[任务][task]执行过程中抛出[指定异常][extension]时重试[指定的次数][count]
 * @author dongjak
 * @created 2024/09/16
 * @version 1.0
 * @since 1.0
 * @param task  任务
 * @param count   重试次数
 * @param interval   重试间隔
 * @param exception    在任务抛出此异常时才会重试
 */
fun <V> retry(
    count: Int,
    interval: Long = 1,
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
                if (i == count) throw e
            } else throw e
        }
        TimeUnit.SECONDS.sleep(interval)
    }
    return res
}

/**
 * 在一个独立的线程中执行异步任务,任务执行完成或**收到中断信号(捕获到[InterruptedException])** 后自动关闭线程池
 * @param task Function0<Unit> 任务
 * @param threadName String 执行任务的线程的名称
 */
fun <V> async(threadName: String = "Async Task", task: () -> V): Future<V> {
    val executor = NamedExecutors.newSingleThreadExecutor(threadName)
    return executor.submit(Callable {
        try {
            task()
        } finally {
            executor.shutdownNow()
        }
    })
}

object NamedExecutors {

    private fun getNamedThreadFactory(nameFormat: String): ThreadFactory =
        ThreadFactoryBuilder().setNamePrefix("${nameFormat}-%d").build()

    fun newSingleThreadExecutor(threadName: String): ExecutorService =
        Executors.newSingleThreadExecutor(getNamedThreadFactory(threadName))

    fun newFixedThreadPool(poolSize: Int, threadName: String): ExecutorService =
        Executors.newFixedThreadPool(poolSize, getNamedThreadFactory(threadName))

    fun newSingleThreadScheduledExecutor(threadName: String): ScheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor(getNamedThreadFactory(threadName))
}
