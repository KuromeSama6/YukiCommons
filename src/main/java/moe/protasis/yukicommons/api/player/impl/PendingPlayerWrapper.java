package moe.protasis.yukicommons.api.player.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;

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
    public String GetName() {
        return "";
    }
}
