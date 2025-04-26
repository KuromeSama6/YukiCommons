package moe.protasis.yukicommons.impl.nms.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.nms.event.*;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.ReflectionUtil;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventPacketListenerSpigot implements IPacketEventPacketListener, PacketListener {
    private final Map<PacketTypeCommon, Set<RegisteredNMSPacketHandler>> packetListeners = new ConcurrentHashMap<>();

    public PacketEventPacketListenerSpigot() {
        PacketEvents.getAPI().getEventManager().registerListener(this, PacketListenerPriority.NORMAL);
    }

    @Override
    public void Register(INMSPacketListener listener, IAbstractPlugin plugin) {
        for (var method : ReflectionUtil.GetAllMethodsRecursive(listener.getClass())) {
            var handler = method.getAnnotation(NMSPacketHandler.class);
            if (handler == null) continue;

            var args = method.getParameterTypes();
            if (args.length != 1) {
                plugin.GetLogger().warning("Could not register NMS packet handler %s: Expected 1 argument.".formatted(method));
                continue;
            }
            var packetClass = args[0];
            if (!PacketWrapper.class.isAssignableFrom(packetClass)) {
                plugin.GetLogger().warning("Could not register NMS packet handler %s: Expected PacketWrapper<?> argument.".formatted(method));
                continue;
            }

            var packetHandler = new RegisteredNMSPacketHandler(listener, plugin, handler, packetClass, method);
            method.setAccessible(true);
            // validate and cache
            var type = packetHandler.GetPacketType();

            if (!packetListeners.containsKey(type)) {
                packetListeners.put(type, new HashSet<>());
            }

            var listeners = packetListeners.get(type);
            if (listeners != null) {
                listeners.add(packetHandler);
            }
        }
    }

    @Override
    public void Unregister(INMSPacketListener listener) {
        for (var set : packetListeners.values()) {
            set.removeIf(c -> c.getListener() == listener);
        }
    }

    @Override
    public void UnregisterAll(IAbstractPlugin plugin) {
        for (var set : packetListeners.values()) {
            set.removeIf(c -> c.getPlugin() == plugin);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        var packet = event.getPacketType();
        var set = packetListeners.get(packet);
        if (set == null) return;

        for (var handler : set) {
            if (handler.GetPacketType() == packet && (handler.GetDirection() == NMSPacketDirection.BOTH || handler.GetDirection() == NMSPacketDirection.INBOUND)) {
                handler.Call(event, handler.getHandlerAnnotation().async(), YukiCommonsBukkit.getInstance().GetScheduler());
            }
        }

    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        var packet = event.getPacketType();
        var set = packetListeners.get(packet);
        if (set == null) return;

        for (var handler : set) {
            if (handler.GetPacketType() == packet && (handler.GetDirection() == NMSPacketDirection.BOTH || handler.GetDirection() == NMSPacketDirection.OUTBOUND)) {
                handler.Call(event, handler.getHandlerAnnotation().async(), YukiCommonsBukkit.getInstance().GetScheduler());
            }
        }
    }

}
