package jzeus.rpc

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.googlecode.jsonrpc4j.ProxyUtil
import jzeus.datetime.Timeouts
import java.net.URI


fun <T, R> rpc(clazz: Class<T>, block: T.() -> R): R {
//    val type = T::class.java
    val client = JsonRpcHttpClient(
        URI("http://localhost:8080").toURL()
    )
    client.readTimeoutMillis = Timeouts.hours(24).milliseconds.toInt()

    // 创建服务代理
    val service = ProxyUtil.createClientProxy(
        clazz.getClassLoader(),
        clazz,
        client
    )
    return service.let(block)
}
