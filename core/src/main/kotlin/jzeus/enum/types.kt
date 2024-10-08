package jzeus.enum

class EnumMap(entries: List<Pair<String, Any>> = emptyList()) : HashMap<String, Any>() {
    init {
        putAll(entries)
    }
}
