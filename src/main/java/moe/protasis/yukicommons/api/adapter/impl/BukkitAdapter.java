package moe.protasis.yukicommons.api.adapter.impl;

import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.impl.BukkitPlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public class BukkitAdapter implements IAdapter {
    @Override
    public IAbstractPlayer Adapt(Object obj) {
        if (obj instanceof Player) return new BukkitPlayerWrapper((Player)obj);
        else if (obj instanceof PlayerEvent) return Adapt(((PlayerEvent)obj).getPlayer());
        else if (obj instanceof EntityEvent) return Adapt(((EntityEvent)obj).getEntity());
        else if (obj instanceof UUID) return Adapt(Bukkit.getPlayer((UUID)obj));
        return null;
    }
}
