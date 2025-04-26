package moe.protasis.yukicommons.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.world.AABB;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerWrapper implements IAbstractPlayer {
    @Getter
    private final Player player;
    private final BukkitPlayerScoreboard scoreboard;

    public BukkitPlayerWrapper(final Player player) {
        this.player = player;
        scoreboard = new BukkitPlayerScoreboard(this);
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
    public void Destroy() {
        scoreboard.Destroy();
    }

    @Override
    public Player GetBukkitPlayer() {
        return player;
    }
}
