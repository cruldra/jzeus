package jzeus.progress

import com.fasterxml.jackson.annotation.JsonIgnore
import jzeus.datetime.gt
import jzeus.task.NamedExecutors
import jzeus.task.async
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 该类用于描述进度信息,例如:在网络下载过程中,通过给出[总字节数][total]和[当前已下载的字节数][current],然后通过[get]方法来获取当前进度值
 * ---
 * 通过调用[prettyFormat]来输出一个易于阅读的进度描述字符串,并且可以通过参数**formatter**来指定自定义的格式化规则
 *
 * @property total 传输总量
 * @property current 当前传输量
 * @property speed 传输速度(单位为mb)
 * @author dongjak
 * @since 1.0
 */
class Progress(
    var total: AtomicLong = AtomicLong(1),
    var current: AtomicLong = AtomicLong(0),
    var speed: String? = null,
    var label: String? = null,
) {
    companion object {
        @JvmStatic
        val INTEGRAL = Progress(1, 1)

        @JvmStatic
        val ZERO = Progress(1, 0)

        @JvmStatic
        val DONE = Progress(1, 1)

        /**
         * 从"$current/$total"格式的字符串创建[Progress]
         * @param str  "$current/$total"格式的字符串
         * @return [Progress]
         */
        @JvmStatic
        fun parse(str: String) = str.split("/").let {
            Progress(it[1].toLong(), it[0].toLong())
        }
    }

    constructor(total: Number, current: Number) : this(AtomicLong(total.toLong()), AtomicLong(current.toLong()))


    /**
     * 设置进度总数
     * @param value 进度总数值
     */
    fun setTotal(value: Long) {
        this.total = AtomicLong(value)
    }

    /**
     * 设置当前进度值
     * @param value 当前进度值
     */
    fun setCurrent(value: Long) {
        this.current = AtomicLong(value)
    }

    @JsonIgnore
    var publisher: ProgressPublisher? = null

    /**
     * 使用指定的值来更新进度
     * @param current 当前进度值
     */
    fun update(current: Long) {
        this.current = AtomicLong(current)
    }


    /**
     * 自动更新进度值,即进度值加1
     * @return Long
     */
    fun update() = this.current.incrementAndGet()


    /**
     * 进度设置为已完成
     */
    fun finished() {
        this.current = this.total
    }

    /**
     * 指示当前进度是否已经完成
     * @return Boolean
     */
    fun isFinished() = this.current.toLong() - this.total.toLong() >= 0

    /**
     * 获取用于描述进度的字符串值,该值介于0~100之间
     */
    fun get() = calc(total, current.toLong() * 100)

    /**
     * 计算百分比
     * @param total Number 总数
     * @param current Number 当前进度
     * @param decimal Int 需要保留的小数位数
     * @return Double 百分比值 1.0~100.0之间
     */
    private fun calc(total: Number, current: Number, decimal: Int = 2): Double =
        String.format(
            "%.${decimal}f",
            current.toDouble() / total.toDouble()
        ).toDouble()

    /**
     * 打印成易于阅读的格式
     * @param formatter 自定义格式化器
     * @return String
     */
    fun prettyFormat(
        formatter: (total: Long, current: Long) -> String = { total, current -> "${current}/${total} ${get()}% ${speed ?: ""}" },
    ): String = formatter(total.toLong(), current.toLong())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Progress

        if (total.get() != other.total.get()) return false
        if (current.get() != other.current.get()) return false

        return true
    }

    override fun hashCode(): Int {
        var result = total.get().hashCode()
        result = 31 * result + current.get().hashCode()
        return result
    }


    /**
     * 创建一个新的进度发布器然后发布此进度
     * @param interval Long 发布间隔,单位为秒
     * @param timeout Long 超时时长,单位为分钟
     * @param threadName String 发布线程的名称
     * @return Progress
     */
    fun publish(interval: Long, timeout: Long, threadName: String): Progress {
        if (publisher == null) {
            publisher = ProgressPublisher(this, threadName) { progress, _ ->
                LoggerFactory.getLogger(ProgressPublisher::class.java).info(progress.prettyFormat())
            }
            publisher!!.publish(Duration.ofSeconds(interval), Duration.ofMinutes(timeout))
        }
        return this
    }

}

/**
 * 进度发布器
 * @property progress Progress 进度
 * @property onProgress Consumer<Progress> 进度回调
 * @property future ScheduledFuture<*>? 计划任务
 * @property threadName String 进度发布线程的名称
 * @constructor
 */
class ProgressPublisher(
    val progress: Progress,
    val threadName: String? = null,
    val onProgress: (progress: Progress, publisher: ProgressPublisher) -> Unit
) {

    private var executors: ScheduledExecutorService? = null
    val id: String = UUID.randomUUID().toString()

    private var future: ScheduledFuture<*>? = null

    private var firstPublishTime: LocalDateTime? = null //进度首次发布的时间点

    /**
     * 发布进度,在进度达到100%时会自动停止发布
     * @param interval Duration 发布间隔
     * @param delay Duration  延迟指定的时间后再发布,默认不延迟,即立即发布
     * @param timeout Duration 如果在指定的时间后进度仍然没有完成,则强制终止进度的发布,默认为30秒
     * @param stopOnFinished Boolean 指示是否在上一次发布的进度达到100%之后不再发布最新进度,某些情况下将子任务的进度作为父任务的进度发布以便更直观感受任务执行过程时可能会用到
     */
    fun publish(
        interval: Duration,
        timeout: Duration = Duration.ofSeconds(30),
        delay: Duration = Duration.ofMillis(0),
        stopOnFinished: Boolean = true
    ): ProgressPublisher {
        executors = executors ?: threadName?.let { NamedExecutors.newSingleThreadScheduledExecutor(threadName) }
                ?: Executors.newSingleThreadScheduledExecutor()

        future = future ?: executors?.scheduleAtFixedRate({
            onProgress(progress, this)//进度发布回调
            firstPublishTime = firstPublishTime ?: LocalDateTime.now() //记录首次发布的时间

            //进度达到100%或者发布超时后取消发布
            if ((progress.isFinished() && stopOnFinished)
                || Duration.between(firstPublishTime, LocalDateTime.now()).gt(timeout)
            ) cancel()
        }, delay.toMillis(), interval.toSeconds(), TimeUnit.SECONDS)

        return this

    }

    /**
     * 取消进度发布
     */
    fun cancel(delay: Duration? = null) {
        fun doCancel() {
            future?.cancel(true)
            executors?.shutdownNow()
            executors = null
        }
        if (delay != null) async { doCancel() } else doCancel()
    }

}
