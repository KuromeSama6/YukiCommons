package moe.protasis.yukicommons.bukkit.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements IJsonTypeAdapter, JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public JsonElement serialize(ItemStack in, Type type, JsonSerializationContext ctx) {
        return ctx.serialize(YukiCommonsBukkit.getInstance().getVersionAdaptor().SerializeItem(in));
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return YukiCommonsBukkit.getInstance().getVersionAdaptor().DeserializeItem(jsonElement.getAsString());
    }

    @Override
    public Class<?> GetTargetType() {
        return ItemStack.class;
    }
}
