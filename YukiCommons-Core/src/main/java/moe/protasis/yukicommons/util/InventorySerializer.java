package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.json.JsonWrapper;
import moe.protasis.yukicommons.nms.IVersionAdaptor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, ItemStack> GetItems(JsonWrapper data) {
        Map<Integer, ItemStack> ret = new HashMap<>();
        var version = IVersionAdaptor.Get();
        if (version == null)
            throw new UnsupportedOperationException("No version adaptor found for this server version");

        for (String key : data.GetKeys()) {
            int slot = Integer.parseInt(key);
            ret.put(slot, version.DeserializeItem(data.GetString(key)));
        }

        return ret;
    }

    public static JsonWrapper ParseLegacy(YamlConfiguration cfg) {
        var inv = Bukkit.createInventory(null, InventoryType.PLAYER);
        var inventory = cfg.getConfigurationSection("inventory");
        for (var key : inventory.getKeys(false)) {
            inv.setItem(Integer.parseInt(key), inventory.getItemStack(key));
        }
        var armor = cfg.getConfigurationSection("armor");
        for (var key : armor.getKeys(false)) {
            inv.setItem(Integer.parseInt(key) + 36, armor.getItemStack(key));
        }

        return Read(inv);
    }

}
