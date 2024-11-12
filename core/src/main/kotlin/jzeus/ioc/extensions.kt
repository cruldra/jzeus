package jzeus.ioc

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.PicoContainer
import org.picocontainer.adapters.AbstractAdapter
import java.lang.reflect.Type

inline fun <reified T : Any> DefaultPicoContainer.adapter(
    crossinline instanceCreator: () -> T
) {
    addAdapter(object : AbstractAdapter<T>(
        T::class.java,
        T::class.java
    ) {
        override fun getComponentInstance(container: PicoContainer?, into: Type?): T {
            return instanceCreator()
        }

        override fun verify(container: PicoContainer) {}

        override fun getDescriptor(): String = "${T::class.java.simpleName}-"
    })
}
