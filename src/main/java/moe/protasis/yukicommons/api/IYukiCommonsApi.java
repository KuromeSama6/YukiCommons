package moe.protasis.yukicommons.api;

import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBukkit;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBungeecord;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;
import org.apache.commons.lang.NotImplementedException;

public interface IYukiCommonsApi {
    void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz);
    default void RegisterCommands(IAbstractPlugin plugin, String pkg) {
        CommandProvider.getInstance().RegisterCommands(plugin, pkg);
    }

    static IYukiCommonsApi Get() {
        if (Util.GetEnvironment() == EnvironmentType.PROXY) return new YukiCommonsApiBungeecord();
        if (Util.GetEnvironment() == EnvironmentType.SPIGOT) return new YukiCommonsApiBukkit();

        throw new RuntimeException("No api could be acquired");
    }
}
