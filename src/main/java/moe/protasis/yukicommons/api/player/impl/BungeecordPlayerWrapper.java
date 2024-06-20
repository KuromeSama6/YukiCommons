package moe.protasis.yukicommons.api.player.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@AllArgsConstructor
public class BungeecordPlayerWrapper implements IAbstractPlayer {
    @Getter
    private final ProxiedPlayer player;

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
}
