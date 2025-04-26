package moe.protasis.yukicommons.util.eventbus;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// EventBus5
public class EventBus5<T1, T2, T3, T4, T5> {
    private final Map<Long, Listener<T1, T2, T3, T4, T5>> subscribers = new ConcurrentHashMap<>();
    private long counter = 1;

    public Long Subscribe(Listener<T1, T2, T3, T4, T5> subscriber) {
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

    public void Unsubscribe(Listener<T1, T2, T3, T4, T5> subscriber) {
        for (Map.Entry<Long, Listener<T1, T2, T3, T4, T5>> entry : new ArrayList<>(subscribers.entrySet())) {
            if (entry.getValue().equals(subscriber)) {
                subscribers.remove(entry.getKey());
            }
        }
    }

    public void Post(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) {
        for (Listener<T1, T2, T3, T4, T5> subscriber : subscribers.values()) {
            try {
                subscriber.Handle(arg1, arg2, arg3, arg4, arg5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface Listener<T1, T2, T3, T4, T5> {
        void Handle(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5);
    }
}
