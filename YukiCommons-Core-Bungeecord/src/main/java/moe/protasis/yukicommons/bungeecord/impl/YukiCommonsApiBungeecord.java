package moe.protasis.yukicommons.bungeecord.impl;

import moe.protasis.yukicommons.api.IYukiCommonsAPI;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.impl.json.serializer.DateTimeSerializer;
import moe.protasis.yukicommons.impl.json.serializer.JsonWrapperSerializer;
import moe.protasis.yukicommons.bungeecord.impl.player.BungeecordAutoPlayerLoadData;
import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.util.EnvironmentType;
import net.md_5.bungee.api.ProxyServer;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class YukiCommonsApiBungeecord implements IYukiCommonsAPI {
    private static final Collection<IJsonTypeAdapter> jsonTypeAdapters = List.of(
            new DateTimeSerializer(),
            new JsonWrapperSerializer()
    );

    @Override
    public EnvironmentType GetEnvironment() {
        return EnvironmentType.BUNGEECORD;
    }
    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin) {
        YukiCommonsBungee.getInstance().getAutoPlayerLoadData().add(new BungeecordAutoPlayerLoadData(clazz, plugin));
    }


    @Override
    public Logger GetLogger() {
        return ProxyServer.getInstance().getLogger();
    }

    @Override
    public IPacketEventPacketListener GetPacketEventPacketListener() {
        return null;
    }

    @Override
    public Collection<IJsonTypeAdapter> GetJsonTypeAdapters() {
        return jsonTypeAdapters;
    }

    @Override
    public IVersionAdaptor GetVersionAdaptor() {
        throw new UnsupportedOperationException("Not supported on this platform");
    }

    @Override
    public IAdaptor GetAdaptor() {
        return YukiCommonsBungee.getInstance().getAdaptor();
    }
}
