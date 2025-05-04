package moe.protasis.yukicommons.velocity.impl.adapter;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import moe.protasis.yukicommons.velocity.impl.YukiCommonsVelocity;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.IWrappedPlayer;
import moe.protasis.yukicommons.velocity.impl.command.ConsoleCommandExecutor;
import moe.protasis.yukicommons.velocity.impl.command.PlayerCommandExecutor;
import moe.protasis.yukicommons.velocity.impl.player.VelocityPlayerWrapper;

import java.util.UUID;

public class VelocityAdaptor implements IAdaptor {
    @Override
    public IAbstractPlayer AdaptToPlayer(Object obj) {
        if (obj instanceof IWrappedPlayer) return ((IWrappedPlayer)obj).GetPlayer();
        if (obj instanceof IAbstractPlayer) return (IAbstractPlayer)obj;
        if (obj instanceof Player p) return VelocityPlayerWrapper.Get(p);
        if (obj instanceof IAbstractCommandExecutor executor) return AdaptToPlayer(executor.GetPlayer());
        if (obj instanceof UUID) return AdaptToPlayer(YukiCommonsVelocity.getInstance().getServer().getPlayer((UUID)obj));

        return null;
    }

    @Override
    public IAbstractCommandExecutor AdaptToCommandExecutor(Object obj) {
        if (obj instanceof Player) return new PlayerCommandExecutor((Player)obj);
        if (obj == YukiCommonsVelocity.getInstance().getServer().getConsoleCommandSource())
            return new ConsoleCommandExecutor((CommandSource)obj);
        return null;
    }
}
