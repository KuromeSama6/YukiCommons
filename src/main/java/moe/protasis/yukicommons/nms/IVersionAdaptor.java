package moe.protasis.yukicommons.nms;

import moe.protasis.yukicommons.YukiCommonsBukkit;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public interface IVersionAdaptor {
    String SerializeItem(ItemStack itemStack);
    ItemStack DeserializeItem(String item);

    static IVersionAdaptor Get() {
        String versionStr = Bukkit.getServer().getBukkitVersion().split("-")[0].replace(".", "_");
        try {
            return (IVersionAdaptor)Class.forName("moe.protasis.yukicommons.nms.impl.VersionAdaptor_" + versionStr)
                    .getConstructor()
                    .newInstance();

        } catch (Exception e) {
            YukiCommonsBukkit.getInstance().getLogger().severe("An error occured while acquiring a version adaptor. This may not be a error, this may simply mean that YukiCommons is not yet supported on this version.");
            e.printStackTrace();
            return null;
        }
    }
}
