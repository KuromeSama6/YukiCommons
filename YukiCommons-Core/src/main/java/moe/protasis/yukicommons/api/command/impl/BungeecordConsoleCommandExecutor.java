package moe.protasis.yukicommons.api.command.impl;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

@AllArgsConstructor
public class BungeecordConsoleCommandExecutor implements IAbstractCommandExecutor {
    private final CommandSender sender;

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
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public IAbstractPlayer GetPlayer() {
        return null;
    }
}
