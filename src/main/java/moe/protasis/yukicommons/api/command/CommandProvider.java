package moe.protasis.yukicommons.api.command;

import lombok.Getter;
import lombok.var;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.Util;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommandProvider {
    @Getter
    private static CommandProvider instance;

    public CommandProvider() {
        if (instance != null) throw new IllegalStateException();
        instance = this;

    }

    public void RegisterCommands(IAbstractPlugin plugin) {
        for (var clazz : new Reflections(ConfigurationBuilder.build(plugin.getClass().getPackage().getName()))
                .getTypesAnnotatedWith(CommandHandler.class)) {
            if (clazz.isAssignableFrom(ICommandHandler.class)) {
                CommandHandler annotation = clazz.getAnnotation(CommandHandler.class);
                if (annotation.platform() == CommandHandler.ExecutionPlatform.BOTH ||
                        annotation.platform().getEnvironmentType() == Util.GetEnvironment()) {
                    try {
                        ICommandHandler<?> handler = RegisterCommand(clazz, plugin);
                        plugin.GetLogger().info(String.format("Registered command %s", handler));
                    } catch (Exception e) {
                        plugin.GetLogger().severe(String.format("An error occured while registering command %s", clazz));
                        e.printStackTrace();
                    }
                }

            } else {
                plugin.GetLogger().warning(String.format("Could not register command %s because it does not extend ICommandHandler!", clazz));
            }
        }
    }

    protected abstract ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception;

}
