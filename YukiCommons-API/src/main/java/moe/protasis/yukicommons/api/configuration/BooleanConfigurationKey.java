package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class BooleanConfigurationKey extends ConfigurationKey<Boolean> {
    public BooleanConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, boolean defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }
    public BooleanConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, boolean defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public BooleanConfigurationKey(ConfigurationSection config, String key, boolean defaultValue) {
        super(config, key, defaultValue);
    }

    public BooleanConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Boolean GetFallbackValue() {
        return false;
    }
}
