package moe.protasis.yukicommons.bukkit.impl;

import moe.protasis.yukicommons.api.IYukiCommonsAPI;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.bukkit.impl.json.serializer.ItemStackSerializer;
import moe.protasis.yukicommons.bukkit.impl.json.serializer.LocationSerializer;
import moe.protasis.yukicommons.bukkit.impl.json.serializer.PotionEffectSerializer;
import moe.protasis.yukicommons.impl.json.serializer.*;
import moe.protasis.yukicommons.bukkit.impl.player.BukkitAutoPlayerLoadData;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.util.EnvironmentType;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class YukiCommonsApiBukkit implements IYukiCommonsAPI {
    private static final Collection<IJsonTypeAdapter> jsonTypeAdapters = List.of(
            new DateTimeSerializer(),
            new JsonWrapperSerializer(),
            new ItemStackSerializer(),
            new LocationSerializer(),
            new PotionEffectSerializer()
    );

    @Override
    public EnvironmentType GetEnvironment() {
        return EnvironmentType.BUKKIT;
    }

    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin) {
        YukiCommonsBukkit.getInstance().getAutoPlayerLoadData().add(new BukkitAutoPlayerLoadData(clazz, plugin));
    }

    @Override
    public Logger GetLogger() {
        return Bukkit.getLogger();
    }

    @Override
    public IPacketEventPacketListener GetPacketEventPacketListener() {
        return YukiCommonsBukkit.getInstance().getNmsPacketListener();
    }

    @Override
    public Collection<IJsonTypeAdapter> GetJsonTypeAdapters() {
        return jsonTypeAdapters;
    }

    @Override
    public IVersionAdaptor GetVersionAdaptor() {
        return YukiCommonsBukkit.getInstance().getVersionAdaptor();
    }

    @Override
    public IAdaptor GetAdaptor() {
        return YukiCommonsBukkit.getInstance().getAdaptor();
    }
}
