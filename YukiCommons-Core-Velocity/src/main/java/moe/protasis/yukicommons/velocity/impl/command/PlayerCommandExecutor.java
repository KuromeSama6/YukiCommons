package moe.protasis.yukicommons.velocity.impl.command;

import com.velocitypowered.api.proxy.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.velocity.impl.YukiCommonsVelocity;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public class PlayerCommandExecutor implements IAbstractCommandExecutor {
    private final Player player;

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
        player.sendMessage(Component.text(message));
    }

    @Override
    public IAbstractPlayer GetPlayer() {
        return YukiCommonsVelocity.getInstance().getAdaptor().AdaptToPlayer(player);
    }
}
