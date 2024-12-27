package moe.protasis.yukicommons.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.json.JsonWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class JsonWrapperSerializer implements JsonSerializer<JsonWrapper>, JsonDeserializer<JsonWrapper> {
    @Override
    public JsonElement serialize(JsonWrapper in, Type type, JsonSerializationContext ctx) {
        return in.getJson();
    }

    @Override
    public JsonWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new JsonWrapper(jsonElement);
    }
}
