package moe.protasis.yukicommons.bukkit.impl.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.protasis.yukicommons.api.nms.event.AllowEventSubclass;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
public class BukkitAutoPlayerLoadData extends AutoPlayerLoadData {
    private static final List<Class<? extends Event>> PREVENT_DOUBLE_CALL = ImmutableList.of(
        EntityDamageEvent.class, EntityDamageByBlockEvent.class, EntityDamageByEntityEvent.class
    );
    private final Set<Class<? extends Event>> adaptationFailedWarnings = new HashSet<>();
    private final Set<Class<? extends Event>> calledThisFrame = new HashSet<>();

    public BukkitAutoPlayerLoadData(Class<? extends WrappedPlayer> playerClass, IAbstractPlugin plugin) {
        super(playerClass, plugin);

        plugin.GetScheduler().ScheduleSyncRepeatingTask(this::Update, 0, 1);
    }

    private void Update() {
        // Due to possible dark magic, witchcraft, or wizardry, some events may be called multiple times.
        // To prevent this from causing issues, we will only call each event once per frame.
        calledThisFrame.clear();
    }

    @Override
    protected synchronized void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin) {
        if (!Event.class.isAssignableFrom(eventClass))
            throw new IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event");
        if (!(plugin.GetPlugin() instanceof Plugin))
            throw new IllegalArgumentException("Plugin must be a Bukkit plugin");
        var superClass = eventClass.getSuperclass();
//        if (!Modifier.isAbstract(superClass.getModifiers()) && method.getAnnotation(AllowEventSubclass.class) == null) {
////            System.out.println("skiped %s, (%s)".formatted(eventClass.getName(), superClass.getName()));
//            return;
//        }

        Plugin bukkitPlugin = (Plugin)plugin.GetPlugin();
        var annotation = method.getAnnotation(EventHandler.class);
        var clazz = (Class<? extends Event>)eventClass;
        Bukkit.getPluginManager().registerEvent(
            clazz,
            new PlayerEventListenerDelegate(method, clazz),
            annotation == null ? EventPriority.NORMAL : annotation.priority(),
            this::CallEvent,
            bukkitPlugin,
            annotation != null && annotation.ignoreCancelled()
        );

//        System.out.println("Registered event handler for %s on %s@%s".formatted(eventClass.getName(), method.getName(), method.getDeclaringClass()));
    }

    private void CallEvent(Listener listener, Event event) {
//        log.info(String.format("evt org call, ls=%s, %s, ts=%s", listener, event, System.currentTimeMillis()));
        if (!(listener instanceof PlayerEventListenerDelegate delegate)) return;
//        System.out.println("call, event type %s, delegate %s".formatted(event.getClass().getName(), delegate.getEventClass().getName()));

        if (!event.getClass().equals(delegate.getEventClass())) return;
//        System.out.println("call %s".formatted(event));

        var player = YukiCommonsBukkit.getInstance().getAdaptor().AdaptToPlayer(event);
        if (player == null) {
            if (!adaptationFailedWarnings.contains(event.getClass())) {
                adaptationFailedWarnings.add(event.getClass());
//                log.warn("Failed to adapt event {} to player", event.getClass().getName());
            }
        }

        CallEventMethod(player, event);
    }
}
