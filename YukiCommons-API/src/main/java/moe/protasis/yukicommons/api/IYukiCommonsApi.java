package moe.protasis.yukicommons.api;

import com.google.gson.JsonSerializer;
import lombok.Getter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The main API class for YukiCommons.
 */
public interface IYukiCommonsApi {
    EnvironmentType GetEnvironment();

    /**
     * Register a WrappedPlayer class to be automatically instantiated when a player
     * logs in.
     *
     * @param clazz  The WrappedPlayer class. This class must have a constructor that
     *               accepts a single IAbstractPlayer object.
     * @param plugin
     */
    void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin);

    /**
     * Registers an NMS packet listener to the given plugin, Bukkit event listener style.
     * @param listener The listener to register.
     * @param plugin The corresponding plugin.
     */
    void RegisterNMSPacketListener(INMSPacketListener listener, IAbstractPlugin plugin);

    Logger GetLogger();
    IPacketEventPacketListener GetPacketEventPacketListener();
    Collection<IJsonTypeAdapter> GetJsonTypeAdapters();

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
}
