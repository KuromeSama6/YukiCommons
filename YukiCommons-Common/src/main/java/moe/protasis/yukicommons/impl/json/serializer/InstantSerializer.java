package moe.protasis.yukicommons.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantSerializer implements IJsonTypeAdapter, JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.ofEpochMilli(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toEpochMilli());
    }

    @Override
    public Class<?> GetTargetType() {
        return Instant.class;
    }
}
