package jzeus.enum

import cn.hutool.core.util.ReflectUtil
import java.util.*

fun Enum<*>.toKebabCase() = this.name.lowercase(Locale.getDefault()).replace("_", "-")
/**
 * 把枚举转换成Map
 *
 * ```kotlin
 *  enum class Color(val value: String,val label:String,val textColor:String) {
 *
 *     RED("red","红色","white"),
 *     GREEN("green","绿色","white"),
 *     BLUE("blue","蓝色","white"),
 *     GRAY("gray","灰色","white"),
 *     BLACK("black","黑色","white"),
 *     WHITE("white","白色","black"),
 * }
 *  val map = Color.RED.toMap()
 *  //result {value=red,label=红色,textColor=white}
 *  ```
 *
 *
 */
fun Enum<*>.toMap(): EnumMap {
    return EnumMap(ReflectUtil.getFields(this::class.java).filterNot {
        it.type.isAssignableFrom(this::class.java)
    }.filterNot {
        it.name == "\$VALUES" || it.name == "\$ENTRIES" || it.name == "jrAlreadyPatched"
    }.map { it.name }.map {
        val field = ReflectUtil.getField(this::class.java, it)
        field.isAccessible = true
        it to field.get(this)
    })
}
