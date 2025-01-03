package moe.protasis.yukicommons.api;

import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBukkit;
import moe.protasis.yukicommons.api.impl.YukiCommonsApiBungeecord;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;

import java.util.logging.Logger;

/**
 * The main API class for YukiCommons.
 */
public interface IYukiCommonsApi {
    /**
     * Register a WrappedPlayer class to be automatically instantiated when a player
     * logs in.
     *
     * @param clazz  The WrappedPlayer class. This class must have a constructor that
     *               accepts a single IAbstractPlayer object.
     * @param plugin
     */
    void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin);

    Logger GetLogger();

    /**
     * Registers all command handlers (classes marked with CommandHandler) to the
     * provided plugin by recursively scanning the given package name.
     * 
     * @param plugin The providing plugin.
     * @param pkg    The package in which to scan for.
     */
    default void RegisterCommands(IAbstractPlugin plugin, String pkg) {
        CommandProvider.getInstance().RegisterCommands(plugin, pkg);
    }

    /**
     * Acquires an instance of the YukiCommons API.
     * 
     * @return The API.
     * @throws RuntimeException If no API object could be acquired under the current
     *                          envrionment.
     */
    static IYukiCommonsApi Get() {
        if (Util.GetEnvironment() == EnvironmentType.PROXY)
            return new YukiCommonsApiBungeecord();
        if (Util.GetEnvironment() == EnvironmentType.SPIGOT)
            return new YukiCommonsApiBukkit();

        throw new RuntimeException("No api could be acquired");
    }
}
