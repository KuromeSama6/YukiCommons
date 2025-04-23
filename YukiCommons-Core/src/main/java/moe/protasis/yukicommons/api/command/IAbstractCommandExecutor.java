package moe.protasis.yukicommons.api.command;

import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;

/**
 * An interface that represents an abstract command executor on any platform.
 */
public interface IAbstractCommandExecutor {
    /**
     * @return If this command executor is a player.
     */
    boolean IsPlayer();

    /**
     * @return If this command executor is the console.
     */
    boolean IsConsole();

    /**
     * Sends a message to this command executor.
     * 
     * @param message The message.
     */
    void SendMessage(String message);

    /**
     * Gets the underlying abstract player of this command executor.
     * 
     * @return The underlying abstract player.
     */
    IAbstractPlayer GetPlayer();
}
