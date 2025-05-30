package moe.protasis.yukicommons.bukkit.impl.adapter;

import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.bukkit.impl.command.BukkitConsoleCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.IWrappedPlayer;
import moe.protasis.yukicommons.bukkit.impl.command.BukkitPlayerCommandExecutor;
import moe.protasis.yukicommons.bukkit.impl.player.BukkitPlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public class BukkitAdaptor implements IAdaptor {
    @Override
    public IAbstractPlayer AdaptToPlayer(Object obj) {
        if (obj instanceof IWrappedPlayer) return ((IWrappedPlayer)obj).GetPlayer();
        if (obj instanceof IAbstractPlayer) return (IAbstractPlayer)obj;
        if (obj instanceof Player) return BukkitPlayerWrapper.Get((Player)obj);
        if (obj instanceof IAbstractCommandExecutor executor) return AdaptToPlayer(executor.GetPlayer());
        if (obj instanceof PlayerEvent) return AdaptToPlayer(((PlayerEvent)obj).getPlayer());
        if (obj instanceof EntityEvent) return AdaptToPlayer(((EntityEvent)obj).getEntity());

        if (obj instanceof BlockPlaceEvent) return AdaptToPlayer(((BlockPlaceEvent)obj).getPlayer());
        if (obj instanceof BlockBreakEvent) return AdaptToPlayer(((BlockBreakEvent)obj).getPlayer());

        if (obj instanceof InventoryEvent) return AdaptToPlayer(((InventoryEvent)obj).getView().getPlayer());

        if (obj instanceof UUID) return AdaptToPlayer(Bukkit.getPlayer((UUID)obj));

        return null;
    }

    @Override
    public IAbstractCommandExecutor AdaptToCommandExecutor(Object obj) {
        if (obj instanceof ConsoleCommandSender) return new BukkitConsoleCommandExecutor((ConsoleCommandSender)obj);
        if (obj instanceof Player) return new BukkitPlayerCommandExecutor(AdaptToPlayer(obj));

        return null;
    }
}
