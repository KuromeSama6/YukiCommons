package moe.protasis.yukicommons.api.adapter;

import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;

/**
 * <code>IAdapter</code> is an interface for handling different abstract objects
 * that may exist on different platforms. For instance, the
 * <code>IAbstractPlayer</code> which represents a player either on Spigot or
 * Bungeecord.
 */
public interface IAdaptor {
    /**
     * Returns an IAbstractPlayer representing the corresponding player.
     * Supported objects are: IAbstractPlayer, Player, EntityEvent (whose Entity is a Player),
     * PlayerEvent, UUID (given that player is online), ProxiedPlayer,
     * PendingConnection.
     * 
     * @param obj The player object.
     * @return The resultant abstract player representation. <code>null</code> if
     *         the input object could not be adapted.
     */
    IAbstractPlayer AdaptToPlayer(Object obj);

    /**
     * Returns an IAbstractCommandExecutor representing the corresponding command
     * executor.
     * Supported objects are: IAbstractPlayer, ConsoleCommandSender, Player, ProxiedPlayer,
     * Bungeecord's console command executor obtained via
     * <code>ProxyServer.getInstance().getConsole()</code>.
     * 
     * @param obj The command executor.
     * @return The resultant abstract command executor representation.
     *         <code>null</code> if
     *         the input object could not be adapted.
     */
    IAbstractCommandExecutor AdaptToCommandExecutor(Object obj);

}
