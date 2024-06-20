package moe.protasis.yukicommons.api.command.impl;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerCommandExecutor implements IAbstractCommandExecutor {
    private final IAbstractPlayer player;

    @Override
    public boolean IsPlayer() {
        return true;
    }

    @Override
    public boolean IsConsole() {
        return false;
    }

    @Override
    public void SendMessage(String message) {
        player.SendMessage(message);
    }

    @Override
    public IAbstractPlayer GetPlayer() {
        return player;
    }
}
