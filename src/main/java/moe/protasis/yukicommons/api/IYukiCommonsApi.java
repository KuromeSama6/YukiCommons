package moe.protasis.yukicommons.api;

import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBukkit;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBungeecord;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;
import org.apache.commons.lang.NotImplementedException;

public interface IYukiCommonsApi {
    void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz);
    default void RegisterCommands(IAbstractPlugin plugin) {
        CommandProvider.getInstance().RegisterCommands(plugin);
    }

    static IYukiCommonsApi Get() {
        if (Util.GetEnvironment() == EnvironmentType.PROXY) return new YukiCommonsApiBungeecord();
        if (Util.GetEnvironment() == EnvironmentType.SPIGOT) return new YukiCommonsApiBukkit();

        throw new NotImplementedException("No api could be acquired");
    }
}
