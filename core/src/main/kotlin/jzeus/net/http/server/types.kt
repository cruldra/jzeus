package jzeus.net.http.server

import cn.dev33.satoken.SaManager
import cn.dev33.satoken.config.SaTokenConfig
import cn.dev33.satoken.context.SaTokenContext
import cn.dev33.satoken.context.model.SaRequest
import cn.dev33.satoken.context.model.SaResponse
import cn.dev33.satoken.context.model.SaStorage
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.servlet.model.SaRequestForServlet
import cn.dev33.satoken.servlet.model.SaResponseForServlet
import cn.dev33.satoken.servlet.model.SaStorageForServlet
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.text.AntPathMatcher
import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.HandlerType
import okhttp3.internal.http.HttpMethod

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

class SaTokenAuthHandler : Handler {
    override fun handle(ctx: Context) {
        try {
            // 检查是否需要登录认证
            if (!isExcludedPath(ctx.path())) {
                StpUtil.checkLogin()
            }
        } catch (e: NotLoginException) {
            ctx.status(401).json(
                mapOf(
                    "code" to 401,
                    "msg" to "未登录",
                    "data" to null
                )
            )
            return
        }
    }

    private fun isExcludedPath(path: String): Boolean {
        // 定义不需要登录的路径
        val excludedPaths = listOf(
            "/login",
            "/register",
            "/swagger"
        )
        return excludedPaths.any { path.startsWith(it) }
    }
}

object JavalinRequestHolder {
    private val contextThreadLocal = ThreadLocal<Context>()

    fun setContext(ctx: Context) {
        contextThreadLocal.set(ctx)
    }

    fun getContext(): Context {
        return contextThreadLocal.get() ?: throw IllegalStateException("Context not found in current thread")
    }

    fun clear() {
        contextThreadLocal.remove()
    }
}

class SaTokenForJavalin : SaTokenContext {
    private val pathMatcher = AntPathMatcher()
    override fun getRequest(): SaRequest {
        return SaRequestForServlet(JavalinRequestHolder.getContext().req())
    }

    override fun getResponse(): SaResponse {
        return SaResponseForServlet(JavalinRequestHolder.getContext().res())
    }

    override fun getStorage(): SaStorage {
        return SaStorageForServlet(JavalinRequestHolder.getContext().req())
    }

    override fun matchPath(pattern: String?, path: String?): Boolean {
        // 如果pattern或path为空，返回false
        if (pattern.isNullOrBlank() || path.isNullOrBlank()) {
            return false
        }

        return try {
            // 使用AntPathMatcher进行路径匹配
            pathMatcher.match(pattern, path)
        } catch (e: Exception) {
            // 发生异常时返回false
            false
        }
    }

}

object SaTokenConfigManager {
    fun init() {
        // 注册上下文
        SaManager.setSaTokenContext(SaTokenForJavalin())

        // 配置Sa-Token
        val config = SaTokenConfig().apply {
            tokenName = "satoken"     // token名称
            timeout = 2592000         // token有效期，单位s 默认30天
            activeTimeout = -1      // token临时有效期
            isConcurrent = true       // 是否允许同一账号并发登录
            isShare = true            // 在多人登录同一账号时，是否共用一个token
            tokenStyle = "uuid"       // token风格
            isLog = false             // 是否输出操作日志
        }
        SaManager.setConfig(config)
    }
}
