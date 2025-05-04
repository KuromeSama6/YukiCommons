package moe.protasis.yukicommons.bukkit.impl.player;

import lombok.Getter;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.world.AABB;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BukkitPlayerWrapper implements IAbstractPlayer {
    private static final Map<UUID, BukkitPlayerWrapper> pool = new HashMap<>();

    @Getter
    private final Player player;
    private final BukkitPlayerScoreboard scoreboard;
    private final BukkitPlayerExperienceBar experienceBar;

    private BukkitPlayerWrapper(final Player player) {
        this.player = player;
        scoreboard = new BukkitPlayerScoreboard(this);
        experienceBar = new BukkitPlayerExperienceBar(this);
    }

    public static BukkitPlayerWrapper Get(final Player player) {
        if (pool.containsKey(player.getUniqueId())) {
            return pool.get(player.getUniqueId());
        }
        BukkitPlayerWrapper wrapper = new BukkitPlayerWrapper(player);
        pool.put(player.getUniqueId(), wrapper);
        return wrapper;
    }

    @Override
    public UUID GetUuid() {
        return player.getUniqueId();
    }

    @Override
    public boolean IsSpigot() {
        return true;
    }

    @Override
    public boolean IsOnline() {
        return player.isOnline();
    }

    @Override
    public String GetName() {
        return player.getName();
    }

    @Override
    public AABB GetBoundingBox() {
        return YukiCommonsBukkit.getInstance().getVersionAdaptor().GetBoundingBox(player);
    }

    @Override
    public Object GetHandle() {
        return player;
    }

    @Override
    public double GetHealth() {
        return player.getHealth();
    }

    @Override
    public void SendMessage(String msg) {
        player.sendMessage(msg);
    }

    @Override
    public boolean HasPermission(String perm) {
        return player.hasPermission(perm);
    }

    @Override
    public int GetPing() {
        return YukiCommonsBukkit.getInstance().getVersionAdaptor().GetPlayerPing(player);
    }

    @Override
    public IScoreboard GetScoreboard() {
        return scoreboard;
    }

    @Override
    public IExperienceBar GetExperienceBar() {
        return experienceBar;
    }

    @Override
    public void Destroy() {
        scoreboard.Destroy();
        experienceBar.Destroy();
        pool.remove(player.getUniqueId());
    }

    @Override
    public Player GetBukkitPlayer() {
        return player;
    }
}
