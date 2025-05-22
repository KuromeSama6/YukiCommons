package moe.protasis.yukicommons.api.nms;

import moe.protasis.yukicommons.api.world.AABB;
import moe.protasis.yukicommons.util.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IVersionAdaptor {
    Lazy<IVersionAdaptor> instance = new Lazy<>(IVersionAdaptor::Get);

    int GetPlayerPing(Player player);
    AABB GetBoundingBox(Entity entity);
    String SerializeItem(ItemStack itemStack);
    ItemStack DeserializeItem(String item);
    ItemStack AddTag(ItemStack item, String key);
    boolean HasTag(ItemStack item, String key);
    ItemStack SetUnbreakable(ItemStack item, boolean unbreakable);

    static IVersionAdaptor Get() {
        if (instance.isInitialized()) return instance.get();

        var versionStr = GetNMSVersion();

        try {
            var ret =  (IVersionAdaptor)Class.forName("moe.protasis.yukicommons.nms.impl.VersionAdaptor_" + versionStr)
                    .getConstructor()
                    .newInstance();
            return ret;

        } catch (Exception e) {
            System.out.println("An error occured while acquiring a version adaptor. This may not be a error, this may simply mean that YukiCommons is not yet supported on this version.");
            e.printStackTrace();
            return null;
        }
    }

    static String GetNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }
}
