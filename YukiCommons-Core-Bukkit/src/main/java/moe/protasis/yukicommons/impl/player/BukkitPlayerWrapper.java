package moe.protasis.yukicommons.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
public class BukkitPlayerWrapper implements IAbstractPlayer {
    @Getter
    private final Player player;

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
    public Player GetBukkitPlayer() {
        return player;
    }
}
