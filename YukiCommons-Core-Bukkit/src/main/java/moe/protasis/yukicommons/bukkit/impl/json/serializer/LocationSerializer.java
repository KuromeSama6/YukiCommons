package moe.protasis.yukicommons.bukkit.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationSerializer implements IJsonTypeAdapter, JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Location.deserialize(jsonDeserializationContext.deserialize(jsonElement, Map.class));
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(location.serialize());
    }

    @Override
    public Class<?> GetTargetType() {
        return Location.class;
    }
}
