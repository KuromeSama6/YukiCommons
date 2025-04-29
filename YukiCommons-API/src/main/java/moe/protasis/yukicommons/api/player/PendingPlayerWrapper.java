package moe.protasis.yukicommons.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.world.AABB;

import java.util.UUID;

@AllArgsConstructor
public class PendingPlayerWrapper implements IAbstractPlayer {
    @Getter
    private final UUID player;
    @Getter
    private final String name;

    public PendingPlayerWrapper(UUID player) {
        this(player, "Unknown-PendingPlayer-%s".formatted(player));
    }

    @Override
    public UUID GetUuid() {
        return player;
    }

    @Override
    public boolean IsSpigot() {
        return false;
    }

    @Override
    public boolean IsOnline() {
        return false;
    }

    @Override
    public double GetHealth() {
        throw new IllegalStateException();
    }

    @Override
    public void SendMessage(String msg) {

    }

    @Override
    public boolean HasPermission(String perm) {
        return false;
    }

    @Override
    public int GetPing() {
        return 0;
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
        return name;
    }

    @Override
    public AABB GetBoundingBox() {
        return AABB.Zero();
    }

    @Override
    public Object GetHandle() {
        return null;
    }
}
