package jzeus.rpc

import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.googlecode.jsonrpc4j.ProxyUtil
import java.net.URL


fun <T,R> rpc(clazz: Class<T>, block: T.() -> R): R {
//    val type = T::class.java
    val client = JsonRpcHttpClient(
        URL("http://localhost:8080")
    )

    // 创建服务代理
    val service = ProxyUtil.createClientProxy(
        clazz.getClassLoader(),
        clazz,
        client
    )
    return service.let(block)
}
