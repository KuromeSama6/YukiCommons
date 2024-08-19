package moe.protasis.yukicommons.util;

import com.google.gson.GsonBuilder;
import lombok.var;
import moe.protasis.yukicommons.json.JsonWrapper;
import moe.protasis.yukicommons.json.serializer.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventorySerializer {
    public InventorySerializer() {
        if (Util.GetEnvironment() != EnvironmentType.SPIGOT)
            throw new IllegalStateException("InventorySerializer not usable on proxy.");
    }

    public JsonWrapper Read(PlayerInventory inv) {
        JsonWrapper data = new JsonWrapper();
        var content = inv.getContents();
        for (int i = 0; i < content.length; i++) {
            ItemStack item = content[i];
            data.Set(Integer.toString(i), item != null ? JsonUtil.SerializeItemstack(item) : null);
        }
        return data;
    }

    public void Write(JsonWrapper data, PlayerInventory inv) {
        ItemStack[] ret = new ItemStack[inv.getContents().length];
        for (String key : data.GetKeys()) {
            int slot = Integer.parseInt(key);
            if (slot >= ret.length) continue;

            ret[slot] = data.GetObject(key, ItemStack.class);
        }

        inv.setContents(ret);
    }
}
