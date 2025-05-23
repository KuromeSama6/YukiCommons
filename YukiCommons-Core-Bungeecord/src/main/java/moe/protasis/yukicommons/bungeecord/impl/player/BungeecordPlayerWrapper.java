package moe.protasis.yukicommons.bungeecord.impl.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.world.AABB;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BungeecordPlayerWrapper implements IAbstractPlayer {
    private final static Map<UUID, BungeecordPlayerWrapper> pool = new java.util.HashMap<>();
    @Getter
    private final ProxiedPlayer player;

    public static BungeecordPlayerWrapper Get(ProxiedPlayer player) {
        if (pool.containsKey(player.getUniqueId())) {
            return pool.get(player.getUniqueId());
        }
        BungeecordPlayerWrapper wrapper = new BungeecordPlayerWrapper(player);
        pool.put(player.getUniqueId(), wrapper);
        return wrapper;
    }

    @Override
    public UUID GetUuid() {
        return player.getUniqueId();
    }

    @Override
    public boolean IsSpigot() {
        return false;
    }

    @Override
    public boolean IsOnline() {
        return player.isConnected();
    }

    @Override
    public double GetHealth() {
        throw new IllegalStateException();
    }

    @Override
    public void SendMessage(String msg) {
        player.sendMessage(new TextComponent(msg));
    }

    @Override
    public String GetName() {
        return player.getName();
    }

    @Override
    public AABB GetBoundingBox() {
        throw new UnsupportedOperationException("BungeeCord does not support bounding boxes");
    }

    @Override
    public Object GetHandle() {
        return player;
    }

    @Override
    public boolean HasPermission(String perm) {
        return player.hasPermission(perm);
    }

    @Override
    public int GetPing() {
        return player.getPing();
    }

    @Override
    public ProxiedPlayer GetProxyPlayer() {
        return player;
    }

    @Override
    public IScoreboard GetScoreboard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IExperienceBar GetExperienceBar() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void Destroy() {
        pool.remove(player.getUniqueId());
    }
}
