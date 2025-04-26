package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class DoubleConfigurationKey extends ConfigurationKey<Double> {
    public DoubleConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, double defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }
    public DoubleConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, double defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public DoubleConfigurationKey(ConfigurationSection config, String key, double defaultValue) {
        super(config, key, defaultValue);
    }

    public DoubleConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Double GetFallbackValue() {
        return 0.0;
    }
}
