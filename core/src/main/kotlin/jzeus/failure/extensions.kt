package jzeus.failure

@Deprecated("推荐使用专用自定义异常")
object Codes {
    /** 这个错误码表示缺少参数 */
    const val MISSING_PARAMETER_CODE = 27301

    /** 这个错误码表示文件不存在 */
    const val FILE_NOT_FOUND_CODE = 10991

    /** 这个错误码表示端口被占用 */
    const val PORT_IS_OCCUPIED_CODE = 15491
}

@Deprecated("推荐使用专用自定义异常")
fun <T> failure(message: String, code: Int = 0, referenceUrl: String? = null, cause: Throwable? = null): T {
    throw Failure(
        code = code,
        message = message,
        referenceUrl = referenceUrl,
        cause = cause
    )
}
