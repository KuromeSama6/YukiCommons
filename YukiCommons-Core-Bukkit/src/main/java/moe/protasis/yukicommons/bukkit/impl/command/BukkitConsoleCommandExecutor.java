package moe.protasis.yukicommons.bukkit.impl.command;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import org.bukkit.command.ConsoleCommandSender;

@AllArgsConstructor
public class BukkitConsoleCommandExecutor implements IAbstractCommandExecutor {
    private final ConsoleCommandSender sender;

    @Override
    public boolean IsPlayer() {
        return false;
    }

    @Override
    public boolean IsConsole() {
        return true;
    }

    @Override
    public void SendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public IAbstractPlayer GetPlayer() {
        return null;
    }
}
