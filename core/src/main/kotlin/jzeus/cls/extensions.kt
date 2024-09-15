package jzeus.cls


/**
 * 判断[this]是否为[clazz]的子类或实现类
 */
fun Class<*>.isSub(clazz: Class<*>) = clazz.isAssignableFrom(this)
