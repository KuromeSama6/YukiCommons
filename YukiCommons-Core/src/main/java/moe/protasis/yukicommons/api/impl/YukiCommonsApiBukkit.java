package moe.protasis.yukicommons.api.impl;

import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.IYukiCommonsApi;
import moe.protasis.yukicommons.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.player.impl.BukkitAutoPlayerLoadData;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class YukiCommonsApiBukkit implements IYukiCommonsApi {
    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin) {
        YukiCommonsBukkit.getInstance().getAutoPlayerLoadData().add(new BukkitAutoPlayerLoadData(clazz, plugin));
    }

    @Override
    public void RegisterNMSPacketListener(INMSPacketListener listener, IAbstractPlugin plugin) {

    }

    @Override
    public Logger GetLogger() {
        return Bukkit.getLogger();
    }

    @Override
    public IPacketEventPacketListener GetPacketEventPacketListener() {
        return null;
    }
}
