package moe.protasis.yukicommons.api.adapter.impl;

import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.impl.BungeecordConsoleCommandExecutor;
import moe.protasis.yukicommons.api.command.impl.PlayerCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.impl.PendingPlayerWrapper;
import moe.protasis.yukicommons.api.player.impl.BungeecordPlayerWrapper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeecordAdapter implements IAdapter {
    public IAbstractPlayer AdaptToPlayer(Object obj) {
        if (obj instanceof ProxiedPlayer) return new BungeecordPlayerWrapper((ProxiedPlayer)obj);
        if (obj instanceof PendingConnection) return new PendingPlayerWrapper(((PendingConnection)obj).getUniqueId());
        if (obj instanceof UUID) return AdaptToPlayer(ProxyServer.getInstance().getPlayer((UUID)obj));

        return null;
    }

    @Override
    public IAbstractCommandExecutor AdaptToCommandExecutor(Object obj) {
        if (obj instanceof ProxiedPlayer) return new PlayerCommandExecutor(IAdapter.Get().AdaptToPlayer(obj));
        if (obj == ProxyServer.getInstance().getConsole()) return new BungeecordConsoleCommandExecutor(ProxyServer.getInstance().getConsole());
        return null;
    }
}
