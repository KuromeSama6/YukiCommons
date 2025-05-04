package moe.protasis.yukicommons.bukkit.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class PlayerEventListenerDelegate implements Listener {
    private final Method method;
    private final Class<? extends Event> eventClass;
}
