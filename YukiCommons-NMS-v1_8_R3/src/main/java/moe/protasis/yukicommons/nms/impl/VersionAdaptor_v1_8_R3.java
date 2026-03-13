package moe.protasis.yukicommons.nms.impl;

import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.api.world.AABB;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VersionAdaptor_v1_8_R3 implements IVersionAdaptor {
    @Override
    public int GetPlayerPing(Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }

    @Override
    public ItemStack SetUnbreakable(ItemStack item, boolean unbreakable) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setBoolean("Unbreakable", unbreakable);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public AABB GetBoundingBox(Entity entity) {
        var bb = ((CraftEntity)entity).getHandle().getBoundingBox();
        return new AABB(bb.a, bb.b, bb.c, bb.d, bb.e, bb.f);
    }

    @Override
    public String SerializeItem(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        var ret = CraftItemStack.asNMSCopy(itemStack);
        if (ret == null) return null;
        return ret.save(new NBTTagCompound()).toString();
    }

    @Override
    public ItemStack DeserializeItem(String item) {
        if (item == null) {
            return null;
        }

        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack.createStack(MojangsonParser.parse(item)));
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
    public ItemStack AddTag(ItemStack item, String key, String value) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString(key, value);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public ItemStack AddTag(ItemStack item, String key, int value) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setInt(key, value);
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

    @Override
    public String GetStringTag(ItemStack item, String key) {
        return GetStringTag(item, key, null);
    }

    @Override
    public String GetStringTag(ItemStack item, String key, String defaultValue) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            return defaultValue;
        }
        return tag.getString(key);
    }

    @Override
    public int GetIntTag(ItemStack item, String key) {
        return GetIntTag(item, key, 0);
    }

    @Override
    public int GetIntTag(ItemStack item, String key, int defaultValue) {
        var nmsItem = CraftItemStack.asNMSCopy(item);
        var tag = nmsItem.getTag();
        if (tag == null) {
            return defaultValue;
        }
        return tag.getInt(key);
    }
}
