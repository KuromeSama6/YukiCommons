package moe.protasis.yukicommons.api.command;

import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;

public interface IAbstractCommandExecutor {
    boolean IsPlayer();
    boolean IsConsole();
    void SendMessage(String message);
    IAbstractPlayer GetPlayer();
}
