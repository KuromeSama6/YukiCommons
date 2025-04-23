package moe.protasis.yukicommons.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.json.JsonWrapper;

import java.lang.reflect.Type;

public class JsonWrapperSerializer implements IJsonTypeAdapter, JsonSerializer<JsonWrapper>, JsonDeserializer<JsonWrapper> {
    @Override
    public JsonElement serialize(JsonWrapper in, Type type, JsonSerializationContext ctx) {
        return in.getJson();
    }

    @Override
    public JsonWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new JsonWrapper(jsonElement);
    }

    @Override
    public Class<?> GetTargetType() {
        return JsonWrapper.class;
    }
}
