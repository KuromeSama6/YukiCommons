package moe.protasis.yukicommons.velocity.impl.player;

import com.velocitypowered.api.proxy.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.world.AABB;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VelocityPlayerWrapper implements IAbstractPlayer {
    private static final Map<UUID, VelocityPlayerWrapper> pool = new HashMap<>();

    private final Player player;

    public static VelocityPlayerWrapper Get(final Player player) {
        if (pool.containsKey(player.getUniqueId())) {
            return pool.get(player.getUniqueId());
        }
        VelocityPlayerWrapper wrapper = new VelocityPlayerWrapper(player);
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
        return player.isActive();
    }

    @Override
    public double GetHealth() {
        return 0;
    }

    @Override
    public void SendMessage(String msg) {
        player.sendMessage(Component.text(msg));
    }

    @Override
    public boolean HasPermission(String perm) {
        return player.hasPermission(perm);
    }

    @Override
    public int GetPing() {
        return Math.max(0, (int)player.getPing());
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

    @Override
    public String GetName() {
        return player.getGameProfile().getName();
    }

    @Override
    public AABB GetBoundingBox() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object GetHandle() {
        return player;
    }
}
