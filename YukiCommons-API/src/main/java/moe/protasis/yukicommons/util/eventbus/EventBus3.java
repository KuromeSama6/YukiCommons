package moe.protasis.yukicommons.util.eventbus;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// EventBus3
public class EventBus3<T1, T2, T3> {
    private final Map<Long, Listener<T1, T2, T3>> subscribers = new ConcurrentHashMap<>();
    private long counter = 1;

    public Long Subscribe(Listener<T1, T2, T3> subscriber) {
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

    public void Unsubscribe(Listener<T1, T2, T3> subscriber) {
        for (Map.Entry<Long, Listener<T1, T2, T3>> entry : new ArrayList<>(subscribers.entrySet())) {
            if (entry.getValue().equals(subscriber)) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    public void Post(T1 arg1, T2 arg2, T3 arg3) {
        for (Listener<T1, T2, T3> subscriber : subscribers.values()) {
            try {
                subscriber.Handle(arg1, arg2, arg3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface Listener<T1, T2, T3> {
        void Handle(T1 arg1, T2 arg2, T3 arg3);
    }
}
