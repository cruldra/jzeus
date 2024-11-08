package jzeus.net.http.server

interface JavalinRouterDefinitions

/**
 * 用于在`http`服务中统一响应格式
 * @property data 数据
 * @property error 错误
 * @author dongjak
 * @created 2024/11/08
 * @version 1.0
 * @since 1.0
 */
data class ResponsePayloads<T>(
    val data: T? = null,
    val error: Error? = null,
) {

    /**
     * 表示一个错误
     * @property type   错误类型
     * @property message 错误信息
     * @property code    错误码
     * @author dongjak
     * @created 2024/11/08
     * @version 1.0
     * @since 1.0
     */
    data class Error(
        val type: String? = null,
        val message: String? = null,
        val code: Int? = null,
    )
}
