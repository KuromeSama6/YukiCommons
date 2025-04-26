package moe.protasis.yukicommons.util.eventbus;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for subscribing to and posting events. This class is thread-safe.
 * @param <T> The type of events to be posted.
 */
public class EventBus1<T> {
    private final Map<Long, Listener<T>> subscribers = new ConcurrentHashMap<>();
    private long counter = 1;

    public Long Subscribe(Listener<T> subscriber) {
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

    public void Unsubscribe(Listener<T> subscriber) {
        for (Map.Entry<Long, Listener<T>> entry : new ArrayList<>(subscribers.entrySet())) {
            if (entry.getValue().equals(subscriber)) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    public void Post(T event) {
        for (Listener<T> subscriber : subscribers.values()) {
            try {
                subscriber.Handle(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static interface Listener<T> {
        void Handle(T event);
    }
}