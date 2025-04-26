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
    public AABB GetBoundingBox(Entity entity) {
        var bb = ((CraftEntity)entity).getHandle().getBoundingBox();
        return new AABB(bb.a, bb.b, bb.c, bb.d, bb.e, bb.f);
    }

    @Override
    public String SerializeItem(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString();
    }

    @Override
    public ItemStack DeserializeItem(String item) {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack.createStack(MojangsonParser.parse(item)));
        } catch (MojangsonParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemStack AddTag(ItemStack item, String key) {
        return null;
    }
}
