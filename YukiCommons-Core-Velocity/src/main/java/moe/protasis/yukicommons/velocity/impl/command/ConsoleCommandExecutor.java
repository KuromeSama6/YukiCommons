package moe.protasis.yukicommons.velocity.impl.command;

import com.velocitypowered.api.command.CommandSource;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public class ConsoleCommandExecutor implements IAbstractCommandExecutor {
    private final CommandSource plugin;

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
        plugin.sendMessage(Component.text(message));
    }

    @Override
    public IAbstractPlayer GetPlayer() {
        return null;
    }
}
