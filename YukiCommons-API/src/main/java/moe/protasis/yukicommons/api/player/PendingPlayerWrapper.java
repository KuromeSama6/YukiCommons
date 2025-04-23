package moe.protasis.yukicommons.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class PendingPlayerWrapper implements IAbstractPlayer {
    @Getter
    private final UUID player;

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
    public String GetName() {
        return "";
    }
}
