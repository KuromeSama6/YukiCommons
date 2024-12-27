package moe.protasis.yukicommons.util;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.var;
import moe.protasis.yukicommons.json.JsonWrapper;
import moe.protasis.yukicommons.json.serializer.ItemStackSerializer;
import moe.protasis.yukicommons.nms.IVersionAdaptor;
import org.bukkit.Utility;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@UtilityClass
public class InventorySerializer {
    public static JsonWrapper Read(Inventory inv) {
        JsonWrapper data = new JsonWrapper();
        var content = inv.getContents();
        var version = IVersionAdaptor.Get();
        if (version == null)
            throw new UnsupportedOperationException("No version adaptor found for this server version");

        for (int i = 0; i < content.length; i++) {
            ItemStack item = content[i];
            data.Set(Integer.toString(i), item != null ? version.SerializeItem(item) : null);
        }
        return data;
    }

    public static void Write(JsonWrapper data, Inventory inv) {
        ItemStack[] ret = new ItemStack[inv.getContents().length];
        var version = IVersionAdaptor.Get();
        if (version == null)
            throw new UnsupportedOperationException("No version adaptor found for this server version");

        for (String key : data.GetKeys()) {
            int slot = Integer.parseInt(key);
            if (slot >= ret.length) continue;

            ret[slot] = version.DeserializeItem(data.GetString(key));
        }

        inv.setContents(ret);
    }
}
