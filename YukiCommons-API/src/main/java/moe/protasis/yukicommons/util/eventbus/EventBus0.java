package moe.protasis.yukicommons.util.eventbus;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a simple event bus that allows zero arguments to be passed to the event handler.
 */
// EventBus0: No arguments
public class EventBus0 {
    private final Map<Long, Listener> subscribers = new ConcurrentHashMap<>();
    private long counter = 1;

    public Long Subscribe(Listener subscriber) {
        long id;
        synchronized (this) {
            id = counter++;
        }
        subscribers.put(id, subscriber);
        return id;
    }

    public void Unsubscribe(Long id) {
        subscribers.remove(id);
    }

    public void UnsubscribeAll() {
        subscribers.clear();
    }

    public void Unsubscribe(Listener subscriber) {
        for (Map.Entry<Long, Listener> entry : new ArrayList<>(subscribers.entrySet())) {
            if (entry.getValue().equals(subscriber)) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    public void Post() {
        for (Listener subscriber : subscribers.values()) {
            try {
                subscriber.Handle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface Listener {
        void Handle();
    }
}

