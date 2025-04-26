package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class IntegerConfigurationKey extends ConfigurationKey<Integer> {
    public IntegerConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, int defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }
    public IntegerConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, int defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public IntegerConfigurationKey(ConfigurationSection config, String key, int defaultValue) {
        super(config, key, defaultValue);
    }

    public IntegerConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Integer GetFallbackValue() {
        return 0;
    }
}
