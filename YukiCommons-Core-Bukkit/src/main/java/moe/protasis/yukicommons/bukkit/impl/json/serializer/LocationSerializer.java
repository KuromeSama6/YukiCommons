package moe.protasis.yukicommons.bukkit.impl.json.serializer;

import com.google.gson.*;
import com.google.gson.stream.JsonToken;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationSerializer implements IJsonTypeAdapter, JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement instanceof JsonObject) {
            return Location.deserialize(jsonDeserializationContext.deserialize(jsonElement, Map.class));
        }

        // new serialization format
        if (jsonElement instanceof JsonPrimitive primitive && primitive.isString()) {
            var str = primitive.getAsString();
            boolean useEntityOffset = str.endsWith("/e");
            if (useEntityOffset) {
                str = str.substring(0, str.length() - 2);
            }

            var mainArgs = str.split(":");
            var worldStr = mainArgs.length > 1 ? mainArgs[0] : "world";

            var args = mainArgs[mainArgs.length > 1 ? 1 : 0].split(",");
            if (args.length < 3)
                throw new JsonParseException("At least 3 arguments are required to deserialize a Location: " + jsonElement);

            try {
                var x = Double.parseDouble(args[0]);
                var y = Double.parseDouble(args[1]);
                var z = Double.parseDouble(args[2]);
                var pitch = args.length > 3 ? Float.parseFloat(args[3]) : 0f;
                var yaw = args.length > 4 ? Float.parseFloat(args[4]) : 0f;

                if (useEntityOffset) {
                    x += 0.5;
                    z += 0.5;
                }

                return new Location(Bukkit.getWorld(worldStr), x, y, z, yaw, pitch);

            } catch (NumberFormatException e) {
                throw new JsonParseException("Failed to parse Location coordinates: " + jsonElement, e);
            }
        }

        throw new JsonParseException("Unsupported Location serialization format: " + jsonElement);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        var str = location.getWorld().getName() + ":" +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getPitch() + "," +
                location.getYaw();
        return new JsonPrimitive(str);
    }

    @Override
    public Class<?> GetTargetType() {
        return Location.class;
    }
}
