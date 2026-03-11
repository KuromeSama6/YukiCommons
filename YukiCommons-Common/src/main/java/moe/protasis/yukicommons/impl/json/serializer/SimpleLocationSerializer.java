package moe.protasis.yukicommons.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.util.SimpleLocation;

import java.lang.reflect.Type;

public class SimpleLocationSerializer implements IJsonTypeAdapter, JsonSerializer<SimpleLocation>, JsonDeserializer<SimpleLocation> {
    @Override
    public SimpleLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement instanceof JsonObject) {
            return jsonDeserializationContext.deserialize(jsonElement, SimpleLocation.class);
        }

        // new serialization format
        if (jsonElement instanceof JsonPrimitive primitive && primitive.isString()) {
            var str = primitive.getAsString();
            return new SimpleLocation(str);
        }

        throw new JsonParseException("Unsupported Location serialization format: " + jsonElement);
    }

    @Override
    public JsonElement serialize(SimpleLocation location, Type type, JsonSerializationContext jsonSerializationContext) {
        var str = location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getPitch() + "," +
                location.getYaw();
        return new JsonPrimitive(str);
    }

    @Override
    public Class<?> GetTargetType() {
        return SimpleLocation.class;
    }
}
