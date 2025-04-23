package moe.protasis.yukicommons.util;

import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class Singletons {
    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public static <T> T Get(Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return (T) instances.get(clazz);
        }

        throw new IllegalStateException("No instance of " + clazz.getName() + " found.");
    }

    public static <T> void Set(T value) {
        var clazz = value.getClass();
        if (instances.containsKey(clazz)) {
            throw new IllegalStateException("Instance of " + clazz.getName() + " already exists.");
        }

        instances.put(clazz, value);
    }

}
