package jzeus.ini

import jzeus.file.toFile
import jzeus.json.objectMapper
import jzeus.str.firstLowerCase
import org.ini4j.Ini

operator fun IniFile.get(section: String = "default", key: String): String? = Ini(this.path.toFile()).get(section, key)
fun String.asIniFile() = IniFile(this)
fun <T> IniFile.toJavaObject(clazz: Class<T>): T {
    val ini = Ini(this.path.toFile())
    val map: Map<String, Map<String, String>> = ini.keys.associateWith { sectionName ->
        (ini[sectionName]?.keys?.associate { key ->
            key.firstLowerCase() to (ini[sectionName]?.get(key).orEmpty())
        } ?: emptyMap())
    }.map {
        it.key.firstLowerCase() to it.value
    }.toMap()
    return objectMapper.convertValue(map, clazz)
}
