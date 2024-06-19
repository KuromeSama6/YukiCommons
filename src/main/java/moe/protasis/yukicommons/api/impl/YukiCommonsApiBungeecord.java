package moe.protasis.yukicommons.api.impl;

import moe.protasis.yukicommons.YukiCommonsBungee;
import moe.protasis.yukicommons.api.IYukiCommonsApi;
import moe.protasis.yukicommons.api.player.WrappedPlayer;

public class YukiCommonsApiBungeecord implements IYukiCommonsApi {
    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz) {
        YukiCommonsBungee.getInstance().getAutoRegisters().add(clazz);
    }
}
