package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class ObjectConfigurationKey extends ConfigurationKey<Object> {
    public ObjectConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, Object defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }
    public ObjectConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, Object defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public ObjectConfigurationKey(ConfigurationSection config, String key, Object defaultValue) {
        super(config, key, defaultValue);
    }

    public ObjectConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Object GetFallbackValue() {
        return null;
    }
}
