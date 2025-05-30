package moe.protasis.yukicommons.nms.impl;

import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.api.world.AABB;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VersionAdaptor_v1_12_R1 implements IVersionAdaptor {
    @Override
    public int GetPlayerPing(Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }

    @Override
    public ItemStack SetUnbreakable(ItemStack item, boolean unbreakable) {
        var ret = item.clone(); // no nms
        var meta = ret.getItemMeta();
        meta.setUnbreakable(unbreakable);
        ret.setItemMeta(meta);
        return ret;
    }

    @Override
    public AABB GetBoundingBox(Entity entity) {
        var bb = ((CraftEntity)entity).getHandle().getBoundingBox();
        return new AABB(bb.a, bb.b, bb.c, bb.d, bb.e, bb.f);
    }

    @Override
    public String SerializeItem(ItemStack itemStack) {
        if (itemStack == null) return null;
        var ret = CraftItemStack.asNMSCopy(itemStack);
        if (ret == null) return null;
        return ret.save(new NBTTagCompound()).toString();
    }

    @Override
    public ItemStack DeserializeItem(String item) {
        if (item == null) return null;
        try {
            return CraftItemStack.asBukkitCopy(new net.minecraft.server.v1_12_R1.ItemStack(MojangsonParser.parse(item)));
        } catch (MojangsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemStack AddTag(ItemStack item, String key) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, "true");
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public boolean HasTag(ItemStack item, String key) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            return false;
        }
        return tag.hasKey(key);
    }
}
