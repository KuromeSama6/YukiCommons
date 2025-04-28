package moe.protasis.yukicommons.bukkit.impl.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BukkitAutoPlayerLoadData extends AutoPlayerLoadData implements Listener {
    @Getter
    private final Listener dummyListener = new DummyListener();
    private final Set<Class<? extends Event>> adaptationFailedWarnings = new HashSet<>();
    private final Set<Class<? extends Event>> multipleCallWarnings = new HashSet<>();
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
    protected void RegisterEvent(Class<?> eventClass, Method method, IAbstractPlugin plugin) {
        if (!Event.class.isAssignableFrom(eventClass))
            throw new IllegalArgumentException("Event class must be a subclass of org.bukkit.event.Event");
        if (!(plugin.GetPlugin() instanceof Plugin))
            throw new IllegalArgumentException("Plugin must be a Bukkit plugin");

        Plugin bukkitPlugin = (Plugin) plugin.GetPlugin();
        var annotation = method.getAnnotation(EventHandler.class);

        Bukkit.getPluginManager().registerEvent(
                (Class<? extends Event>)eventClass,
                this,
                annotation == null ? EventPriority.NORMAL : annotation.priority(),
                this::CallEvent,
                bukkitPlugin,
                annotation != null && annotation.ignoreCancelled()
        );

//        log.info("Registered event handler for {} on {}@{}", eventClass.getName(), method.getName(), method.getDeclaringClass());
    }

    private void CallEvent(Listener listener, Event event) {
//        log.info(String.format("evt org call, ls=%s, %s, ts=%s", listener, event, System.currentTimeMillis()));
//        if (calledThisFrame.contains(event.getClass())) {
//            if (!multipleCallWarnings.contains(event.getClass())) {
//                multipleCallWarnings.add(event.getClass());
//                log.warn("Event {} was called multiple times in the same frame. This may cause some issues. This warning will not be displayed again for this event.", event.getClass().getName());
//            }
//            return;
//        }
//        calledThisFrame.add(event.getClass());

        var player = YukiCommonsBukkit.getInstance().getAdaptor().AdaptToPlayer(event);
        if (player == null) {
            if (!adaptationFailedWarnings.contains(event.getClass())) {
                adaptationFailedWarnings.add(event.getClass());
                log.warn("Failed to adapt event {} to player", event.getClass().getName());
            }
        }

        CallEventMethod(player, event);
    }

    private static class DummyListener implements Listener { }
}
