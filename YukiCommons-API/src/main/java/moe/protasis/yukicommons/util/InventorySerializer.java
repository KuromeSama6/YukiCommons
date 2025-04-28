package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.api.json.JsonWrapper;
import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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

        // older versions: content does not include armor
        if (inv instanceof PlayerInventory playerInventory && content.length <= 36) {
            var armor = playerInventory.getArmorContents();
            for (int i = 0; i < armor.length; i++) {
                ItemStack item = armor[i];
                data.Set(Integer.toString(i + 36), item != null ? version.SerializeItem(item) : null);
            }
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

        // older versions: content does not include armor
        if (inv instanceof PlayerInventory playerInventory && ret.length <= 36) {
            ItemStack[] armor = new ItemStack[4];
            for (String key : data.GetKeys()) {
                int slot = Integer.parseInt(key);
                if (slot < 36 || slot >= 40) continue;

                armor[slot - 36] = version.DeserializeItem(data.GetString(key));
            }
            playerInventory.setArmorContents(armor);
        }

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
