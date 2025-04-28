package moe.protasis.yukicommons.bungeecord.impl.player;

import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

import java.lang.reflect.Method;

public class BungeecordAutoPlayerLoadData extends AutoPlayerLoadData {
    public BungeecordAutoPlayerLoadData(Class<? extends WrappedPlayer> playerClass, IAbstractPlugin plugin) {
        super(playerClass, plugin);
    }

    @Override
    protected void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin) {

    }
}
