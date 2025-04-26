package moe.protasis.yukicommons.api.configuration;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a key-value pair in a specific configuration.
 * It is up to the developer to ensure that any unboxing that occurs when acquiring the value is safe.
 * @param <T> the type of the value associated with the key.
 */
public abstract class ConfigurationKey<T> {
    private final ConfigurationSection config;
    @Getter
    private final String key;
    @Getter
    private final T defaultValue;
    private final boolean createIfNotExists;
    private final boolean cached;

    private T cachedValue;
    private boolean hasCachedValue = false;

    public ConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, T defaultValue) {
        this(config, key, createIfNotExists, defaultValue, false);
    }

    public ConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, T defaultValue, boolean cached) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue == null ? GetFallbackValue() : defaultValue;
        this.createIfNotExists = createIfNotExists;
        this.cached = cached;

        if (!Exists() && createIfNotExists) {
            Set(defaultValue);
        }
    }

    public ConfigurationKey(ConfigurationSection config, String key, T defaultValue) {
        this(config, key, true, defaultValue);
    }

    public ConfigurationKey(ConfigurationSection config, String key) {
        this(config, key, true, null);
    }

    public boolean Exists() {
        if (cached && hasCachedValue) {
            return true;
        }

        return config.isSet(key);
    }

    public T Get() {
        return Get(defaultValue);
    }

    public T Get(Object def) {
        if (cached && hasCachedValue) {
            return cachedValue;
        }

        var ret = (T)config.get(key, def);
        cachedValue = ret;
        return ret;
    }

    public void Set(T value) {
        config.set(key, value);
        if (cached) {
            hasCachedValue = true;
            cachedValue = value;
        }
    }

    protected abstract T GetFallbackValue();

}
