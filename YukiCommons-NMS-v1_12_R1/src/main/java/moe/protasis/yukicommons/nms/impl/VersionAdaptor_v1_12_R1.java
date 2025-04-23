package moe.protasis.yukicommons.nms.impl;

import moe.protasis.yukicommons.nms.IVersionAdaptor;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class VersionAdaptor_v1_12_R1 implements IVersionAdaptor {
    @Override
    public String SerializeItem(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString();
    }

    @Override
    public ItemStack DeserializeItem(String item) {
        try {
            return CraftItemStack.asBukkitCopy(new net.minecraft.server.v1_12_R1.ItemStack(MojangsonParser.parse(item)));
        } catch (MojangsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemStack AddTag(ItemStack item, String key) {
        return null;
    }
}
