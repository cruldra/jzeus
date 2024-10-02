package jzeus.template

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter


fun String.render(vararg contextVariables: Pair<String, Any?>): String {
    val context = VelocityContext()
    contextVariables.forEach {
        context.put(it.first, it.second)
    }
    val writer = StringWriter()
    Velocity.evaluate(context, writer, "mylog", this)
    val result = writer.toString()
    return result
}
