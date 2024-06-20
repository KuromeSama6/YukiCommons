package moe.protasis.yukicommons.api.adapter.impl;

import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.impl.BukkitConsoleCommandExecutor;
import moe.protasis.yukicommons.api.command.impl.PlayerCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.impl.BukkitPlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public class BukkitAdapter implements IAdapter {
    @Override
    public IAbstractPlayer AdaptToPlayer(Object obj) {
        if (obj instanceof Player) return new BukkitPlayerWrapper((Player)obj);
        if (obj instanceof PlayerEvent) return AdaptToPlayer(((PlayerEvent)obj).getPlayer());
        if (obj instanceof EntityEvent) return AdaptToPlayer(((EntityEvent)obj).getEntity());
        if (obj instanceof UUID) return AdaptToPlayer(Bukkit.getPlayer((UUID)obj));

        return null;
    }

    @Override
    public IAbstractCommandExecutor AdaptToCommandExecutor(Object obj) {
        if (obj instanceof ConsoleCommandSender) return new BukkitConsoleCommandExecutor((ConsoleCommandSender)obj);
        if (obj instanceof Player) return new PlayerCommandExecutor(AdaptToPlayer(obj));

        return null;
    }
}
