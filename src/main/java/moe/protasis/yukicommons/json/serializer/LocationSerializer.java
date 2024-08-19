package moe.protasis.yukicommons.json.serializer;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Location.deserialize(jsonDeserializationContext.deserialize(jsonElement, Map.class));
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(location.serialize());
    }
}
