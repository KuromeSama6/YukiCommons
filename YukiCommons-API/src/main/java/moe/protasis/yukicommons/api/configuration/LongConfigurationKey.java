package moe.protasis.yukicommons.api.configuration;

import org.bukkit.configuration.ConfigurationSection;

public class LongConfigurationKey extends ConfigurationKey<Long> {
    public LongConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, long defaultValue, boolean cached) {
        super(config, key, createIfNotExists, defaultValue, cached);
    }
    public LongConfigurationKey(ConfigurationSection config, String key, boolean createIfNotExists, long defaultValue) {
        super(config, key, createIfNotExists, defaultValue);
    }

    public LongConfigurationKey(ConfigurationSection config, String key, long defaultValue) {
        super(config, key, defaultValue);
    }

    public LongConfigurationKey(ConfigurationSection config, String key) {
        super(config, key);
    }

    @Override
    protected Long GetFallbackValue() {
        return 0L;
    }
}
