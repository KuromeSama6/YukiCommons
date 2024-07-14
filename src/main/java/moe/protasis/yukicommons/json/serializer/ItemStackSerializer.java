package moe.protasis.yukicommons.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.YukiCommons;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public JsonElement serialize(ItemStack in, Type type, JsonSerializationContext ctx) {
        return ctx.serialize(YukiCommons.getInstance().getVersionAdaptor().SerializeItem(in));
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return YukiCommons.getInstance().getVersionAdaptor().DeserializeItem(jsonElement.getAsString());
    }
}
