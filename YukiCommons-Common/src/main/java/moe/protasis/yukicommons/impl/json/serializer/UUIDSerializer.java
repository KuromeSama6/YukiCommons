package moe.protasis.yukicommons.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDSerializer implements IJsonTypeAdapter, JsonSerializer<UUID>, JsonDeserializer<UUID> {
    @Override
    public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return UUID.fromString(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(uuid.toString());
    }

    @Override
    public Class<?> GetTargetType() {
        return UUID.class;
    }
}
