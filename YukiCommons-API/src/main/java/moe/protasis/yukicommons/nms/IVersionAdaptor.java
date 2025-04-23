package moe.protasis.yukicommons.nms;

import moe.protasis.yukicommons.util.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public interface IVersionAdaptor {
    Lazy<IVersionAdaptor> instance = new Lazy<>(IVersionAdaptor::Get);

    String SerializeItem(ItemStack itemStack);
    ItemStack DeserializeItem(String item);
    ItemStack AddTag(ItemStack item, String key);

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
