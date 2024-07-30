package moe.protasis.yukicommons.api.player;

import moe.protasis.yukicommons.api.entity.IAbstractLivingEntity;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Represents an abstract player that may exist on any platform.
 */
public interface IAbstractPlayer extends IAbstractLivingEntity {
    UUID GetUuid();

    /**
     * @return Whether this player is a player on a Spigot server (or its forks).
     */
    boolean IsSpigot();

    /**
     * @return Whether this player is online.
     */
    boolean IsOnline();

    /**
     * @return This player's health. Return 0d if this is not applicable to a player
     *         on the current platform.
     */
    double GetHealth();

    /**
     * Sends a text message to this player.
     * 
     * @param msg The message.
     */
    void SendMessage(String msg);

    /**
     * @return The Bukkit Player instance represented by this player, or
     *         <code>null</code> if not running under Bukkit.
     */
    default Player GetBukkitPlayer() {
        return Bukkit.getPlayer(GetUuid());
    }

    /**
     * @return The Bungeecord Player instance represented by this player, or
     *         <code>null</code> if not running under Bungeecord.
     */
    default ProxiedPlayer GetProxyPlayer() {
        return ProxyServer.getInstance().getPlayer(GetUuid());
    }
}
