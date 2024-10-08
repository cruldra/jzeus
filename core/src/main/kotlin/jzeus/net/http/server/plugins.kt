package jzeus.net.http.server

import io.javalin.config.JavalinConfig
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.plugin.Plugin
import java.util.function.Consumer

class AuthenticationPlugin(userConfig: Consumer<Config>? = null) :
    Plugin<AuthenticationPlugin.Config>(userConfig, Config()) {

    data class Config(
        var excludedPaths: Set<String> = setOf(),
        var getPrincipal: (String) -> Any? = { null },
        var getError: (Context) -> Any = { mapOf("error" to "Unauthorized") }
    )

    override fun onStart(config: JavalinConfig) {
        config.router.mount {
            it.before(authenticationHandler)
        }
    }

    private val authenticationHandler = Handler { ctx ->
        if (shouldAuthenticate(ctx)) {
            val token = ctx.header("Authorization")
            var principal: Any? = null
            if (token == null || pluginConfig.getPrincipal(token.removePrefix("Bearer "))
                    .also { principal = it } == null
            ) {
                ctx.status(401).json(pluginConfig.getError(ctx))
                ctx.skipRemainingHandlers()
            }

            if (principal != null) {
                ctx.attribute("principal", principal)
            }

        }
    }

    private fun shouldAuthenticate(ctx: Context): Boolean {
        return !pluginConfig.excludedPaths.contains(ctx.path())
    }

    override fun name() = "AuthenticationPlugin"
}
