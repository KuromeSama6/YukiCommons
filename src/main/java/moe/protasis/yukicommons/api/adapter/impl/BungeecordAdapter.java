package moe.protasis.yukicommons.api.adapter.impl;

import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.impl.BungeecordPlayerWrapper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeecordAdapter implements IAdapter {
    public IAbstractPlayer Adapt(Object obj) {
        if (obj instanceof ProxiedPlayer) return new BungeecordPlayerWrapper((ProxiedPlayer)obj);
        else if (obj instanceof UUID) return Adapt(ProxyServer.getInstance().getPlayer((UUID)obj));
        return null;
    }
}
