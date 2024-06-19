package moe.protasis.yukicommons.api.player;

import moe.protasis.yukicommons.api.entity.IAbstractLivingEntity;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Proxy;
import java.util.UUID;

public interface IAbstractPlayer extends IAbstractLivingEntity {
    UUID GetUuid();
    boolean IsSpigot();

    boolean IsOnline();
    double GetHealth();

    default Player GetBukkitPlayer() {
        return Bukkit.getPlayer(GetUuid());
    }
    default ProxiedPlayer GetProxyPlayer() {
        return ProxyServer.getInstance().getPlayer(GetUuid());
    }
}
