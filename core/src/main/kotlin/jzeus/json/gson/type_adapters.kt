package jzeus.json.gson

import com.google.gson.*
import jzeus.any.Range
import java.lang.reflect.Type

class RangeTypeAdapter : JsonSerializer<Range<*>>, JsonDeserializer<Range<*>> {
    override fun serialize(src: Range<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonArray = JsonArray()
        jsonArray.add(src.min.toString())
        jsonArray.add(src.max.toString())
        return jsonArray
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Range<*> {
        val jsonArray = json.asJsonArray
        if (jsonArray.size() != 2) {
            throw JsonParseException("Expected 2 elements for Range")
        }

        val min = jsonArray[0].asInt
        val max = jsonArray[1].asInt
        return Range(min, max)
    }
}
