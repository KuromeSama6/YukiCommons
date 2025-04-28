package moe.protasis.yukicommons.bungeecord.impl.adapter;

import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.IWrappedPlayer;
import moe.protasis.yukicommons.api.player.PendingPlayerWrapper;
import moe.protasis.yukicommons.bungeecord.impl.command.BungeecordConsoleCommandExecutor;
import moe.protasis.yukicommons.bungeecord.impl.command.PlayerCommandExecutor;
import moe.protasis.yukicommons.bungeecord.impl.player.BungeecordPlayerWrapper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeecordAdaptor implements IAdaptor {
    public IAbstractPlayer AdaptToPlayer(Object obj) {
        if (obj instanceof IWrappedPlayer) return ((IWrappedPlayer)obj).GetPlayer();
        if (obj instanceof IAbstractPlayer) return (IAbstractPlayer)obj;
        if (obj instanceof ProxiedPlayer) return new BungeecordPlayerWrapper((ProxiedPlayer)obj);
        if (obj instanceof PendingConnection) return new PendingPlayerWrapper(((PendingConnection)obj).getUniqueId());
        if (obj instanceof UUID) return AdaptToPlayer(ProxyServer.getInstance().getPlayer((UUID)obj));

        return null;
    }

    @Override
    public IAbstractCommandExecutor AdaptToCommandExecutor(Object obj) {
        if (obj instanceof ProxiedPlayer) return new PlayerCommandExecutor(AdaptToPlayer(obj));
        if (obj == ProxyServer.getInstance().getConsole()) return new BungeecordConsoleCommandExecutor(ProxyServer.getInstance().getConsole());
        return null;
    }
}
