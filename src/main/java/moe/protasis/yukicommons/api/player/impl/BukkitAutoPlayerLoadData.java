package moe.protasis.yukicommons.api.player.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

@Slf4j
public class BukkitAutoPlayerLoadData extends AutoPlayerLoadData {
    @Getter
    private final Listener dummyListener = new DummyListener();

    public BukkitAutoPlayerLoadData(Class<? extends WrappedPlayer> playerClass, IAbstractPlugin plugin) {
        super(playerClass, plugin);
    }

    @Override
    protected void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin) {
        if (!Event.class.isAssignableFrom(eventClass))
            throw new IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event");
        if (!(plugin.GetPlugin() instanceof Plugin))
            throw new IllegalArgumentException("Plugin must be a Bukkit plugin");

        Plugin bukkitPlugin = (Plugin) plugin.GetPlugin();
        var annotation = method.getAnnotation(EventHandler.class);

        Bukkit.getPluginManager().registerEvent(
                (Class<? extends Event>)eventClass,
                new DummyListener(),
                annotation == null ? EventPriority.NORMAL : annotation.priority(),
                (listener, event) -> CallEvent(event),
                bukkitPlugin,
                annotation != null && annotation.ignoreCancelled()
        );

//        log.info("Registered event handler for {}", eventClass.getName());
    }

    private void CallEvent(Event event) {
        var player = IAdapter.Get().AdaptToPlayer(event);
        if (player == null) {
            log.warn("Failed to adapt event {} to player", event.getClass().getName());
            return;
        }

        CallEventMethod(player, event);
    }

    private static class DummyListener implements Listener { }
}
