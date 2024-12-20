package jzeus.task

import cn.hutool.core.thread.ThreadFactoryBuilder
import jzeus.async.sleep
import jzeus.cls.isSub
import jzeus.datetime.Timeout
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
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
 * 满足条件时重试
 *
 * @param on 重试条件
 * @param interval 重试间隔
 * @param task 任务
 */
fun retry(on: (Int) -> Boolean, interval: () -> Unit = {}, task: () -> Unit) {
    var times = 1
    while (on(times)) {
        try {
            task()
            interval()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            times++
        }
    }
}

fun <R> wait(times: Int, interval: Timeout, block: () -> R): R? {
    val res = (1..times).toList().firstNotNullOfOrNull {
        val res = block()
        if (res != null)
            res else {
            sleep(interval.duration.seconds)
            null
        }
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

/**
 * 统计[过程][procedure]的执行时间
 * @param timeUnit 返回的时间长度的单位
 * @param procedure 被统计的过程
 * @return 过程执行时间
 */
fun stopwatch(timeUnit: TemporalUnit = ChronoUnit.MILLIS, procedure: () -> Unit): Long {
    val beginTime = LocalDateTime.now()
    procedure()
    return Duration.between(beginTime, LocalDateTime.now()).let {
        when (timeUnit) {
            ChronoUnit.MILLIS -> it.toMillis()
            ChronoUnit.MINUTES -> it.toMinutes()
            ChronoUnit.HOURS -> it.toHours()
            ChronoUnit.DAYS -> it.toDays()
            else -> it.get(timeUnit)
        }
    }
}

/**
 * 在[timeout]后执行[task]
 */
fun setTimeout(timeout: Timeout, task: () -> Unit) {
    val executor = Executors.newSingleThreadScheduledExecutor()
    executor.schedule({
        task()
        executor.shutdown()
    }, timeout.duration.seconds, TimeUnit.SECONDS)
}
