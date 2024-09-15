package jzeus.ini

import jzeus.file.asFile
import org.ini4j.Ini

operator fun IniFile.get(section: String = "default", key: String): String? = Ini(this.path.asFile()).get(section, key)
fun String.asIniFile() = IniFile(this)
