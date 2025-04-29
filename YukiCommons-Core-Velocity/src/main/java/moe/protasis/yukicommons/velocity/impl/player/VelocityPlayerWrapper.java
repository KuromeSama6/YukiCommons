package moe.protasis.yukicommons.velocity.impl.player;

import com.velocitypowered.api.proxy.Player;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.world.AABB;
import net.kyori.adventure.text.Component;

import java.util.UUID;

@AllArgsConstructor
public class VelocityPlayerWrapper implements IAbstractPlayer {
    private final Player player;

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
