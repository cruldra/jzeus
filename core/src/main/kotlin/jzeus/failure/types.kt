package jzeus.failure

/**
 * 用于代替[Exception],对其进行了如下扩展:
 *
 * * 添加了一个[code][code]属性,用于表示异常代码
 * * 添加了一个[referenceUrl][referenceUrl]属性,用于表示异常参考链接
 * @author dongjak
 * @created 2024/01/29
 * @version 1.0
 * @since 1.0
 * @property code   异常代码
 * @property message  异常消息
 * @property cause  异常原因
 * @property referenceUrl  异常参考链接
 */
@Deprecated("推荐使用专用自定义异常")
open class Failure(
    open val code: Int,
    override val message: String? = null,
    open val referenceUrl: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause)
