package moe.protasis.yukicommons.velocity.impl.player;

import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

import java.lang.reflect.Method;

public class VelocityAutoPlayerLoadData extends AutoPlayerLoadData {
    public VelocityAutoPlayerLoadData(Class<? extends WrappedPlayer> playerClass, IAbstractPlugin plugin) {
        super(playerClass, plugin);
    }

    @Override
    protected void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin) {

    }
}
