package moe.protasis.yukicommons.bukkit.impl.command;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;

@AllArgsConstructor
public class BukkitPlayerCommandExecutor implements IAbstractCommandExecutor {
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