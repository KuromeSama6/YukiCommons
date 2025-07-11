package moe.protasis.yukicommons.velocity.impl;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import moe.protasis.yukicommons.api.IYukiCommonsAPI;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.impl.json.serializer.DateTimeSerializer;
import moe.protasis.yukicommons.impl.json.serializer.JsonWrapperSerializer;
import moe.protasis.yukicommons.impl.json.serializer.UUIDSerializer;
import moe.protasis.yukicommons.velocity.impl.player.VelocityAutoPlayerLoadData;
import moe.protasis.yukicommons.util.EnvironmentType;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class YukiCommonsApiVelocity implements IYukiCommonsAPI {
    private final YukiCommonsVelocity plugin;

    public YukiCommonsApiVelocity(YukiCommonsVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public EnvironmentType GetEnvironment() {
        return EnvironmentType.VELOCITY;
    }

    @Override
    public void AddAutoPlayerRegister(Class<? extends WrappedPlayer> clazz, IAbstractPlugin plugin) {
        YukiCommonsVelocity.getInstance().getAutoPlayerLoadData().add(new VelocityAutoPlayerLoadData(clazz, plugin));
    }

    @Override
    public Logger GetLogger() {
        return plugin.GetLogger();
    }

    @Override
    public IPacketEventPacketListener GetPacketEventPacketListener() {
        return null;
    }

    @Override
    public Collection<IJsonTypeAdapter> GetJsonTypeAdapters() {
        return List.of(
            new DateTimeSerializer(),
            new JsonWrapperSerializer(),
            new UUIDSerializer()
        );
    }

    @Override
    public IVersionAdaptor GetVersionAdaptor() {
        throw new UnsupportedOperationException("Version adapter not supported on Velocity");
    }

    @Override
    public IAdaptor GetAdaptor() {
        return plugin.getAdaptor();
    }
}
