package moe.protasis.yukicommons.api.impl;

import moe.protasis.yukicommons.YukiCommons;
import moe.protasis.yukicommons.api.IYukiCommonsApi;
import moe.protasis.yukicommons.api.player.WrappedPlayer;

public class YukiCommonsApiBukkit implements IYukiCommonsApi {
    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz) {
        YukiCommons.getInstance().getAutoRegisters().add(clazz);
    }
}
