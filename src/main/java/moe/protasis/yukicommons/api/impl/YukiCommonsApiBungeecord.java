package moe.protasis.yukicommons.api.impl;

import moe.protasis.yukicommons.YukiCommonsBungee;
import moe.protasis.yukicommons.api.IYukiCommonsApi;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.player.impl.BungeecordAutoPlayerLoadData;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

public class YukiCommonsApiBungeecord implements IYukiCommonsApi {
    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin) {
        YukiCommonsBungee.getInstance().getAutoPlayerLoadData().add(new BungeecordAutoPlayerLoadData(clazz, plugin));
    }
}
