package moe.protasis.yukicommons.api.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.protasis.yukicommons.api.misc.IDestroyable;
import moe.protasis.yukicommons.api.nms.event.PlayerEventHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.Util;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AutoPlayerLoadData {
    @Getter
    private final Class<? extends WrappedPlayer> playerClass;
    @Getter
    private final Map<Class<?>, Method> playerEventHandlers = new ConcurrentHashMap<>();

    public AutoPlayerLoadData(Class<? extends WrappedPlayer> playerClass, IAbstractPlugin plugin) {
//        log.info("Loading player class {}", playerClass.getName());
        this.playerClass = playerClass;
        // load event handlers
        for (Method method : playerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PlayerEventHandler.class)) continue;
            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Player event handler method must have exactly one parameter");
            }

            var annotation = method.getAnnotation(PlayerEventHandler.class);
            if (!(annotation.platform().IsSupported(Util.GetEnvironment()))) {
                continue;
            }

            Class<?> eventClass = method.getParameterTypes()[0];
            if (playerEventHandlers.containsKey(eventClass)) {
                throw new IllegalArgumentException("Player event handler method for " + eventClass.getName() + " already exists");
            }

            RegisterEvent(eventClass, method, plugin);

            method.setAccessible(true);
            playerEventHandlers.put(eventClass, method);
        }

//        log.info("Loaded player class {}. Found {} event handlers", playerClass.getName(), playerEventHandlers.size());
    }

    protected abstract void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin);

    protected void CallEventMethod(IAbstractPlayer player, Object event) {
        for (var clazz : playerEventHandlers.keySet()) {
            if (clazz.equals(event.getClass())) {
                Method method = playerEventHandlers.get(event.getClass());

                if (method == null) continue;
                try {
                    WrappedPlayer wp = WrappedPlayer.GetPlayer(player, playerClass);
                    if (wp == null) continue;
                    method.invoke(wp, event);

                } catch (IllegalAccessException | InvocationTargetException e) {
                    Bukkit.getLogger().severe("Failed to call event handler for " + event.getClass().getName() + " on " + player.GetName());
                    e.printStackTrace();
                }
            }
        }
    }
}
