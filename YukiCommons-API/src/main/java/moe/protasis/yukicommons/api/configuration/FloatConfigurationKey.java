package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class FloatConfigurationKey extends ConfigurationKey<Float> {
    public FloatConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, float defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }

    public FloatConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, float defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public FloatConfigurationKey(ConfigurationSection config, String key, float defaultValue) {
        super(config, key, defaultValue);
    }

    public FloatConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Float GetFallbackValue() {
        return 0f;
    }
}
