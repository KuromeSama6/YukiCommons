package moe.protasis.yukicommons.api.command;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.var;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.util.Util;

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

    public void RegisterCommands(IAbstractPlugin plugin, String pkg) {
        try (
                ScanResult result = new ClassGraph()
//                        .verbose()
                        .enableAllInfo()
                        .acceptPackages(pkg)
                        .scan();
        ) {
            for (ClassInfo info : result.getClassesWithAnnotation(CommandHandler.class)) {
                Class<?> clazz = info.loadClass();
                if (ICommandHandler.class.isAssignableFrom(clazz)) {
                    CommandHandler annotation = clazz.getAnnotation(CommandHandler.class);
                    if (annotation.platform() == CommandHandler.ExecutionPlatform.BOTH ||
                            annotation.platform().getEnvironmentType() == Util.GetEnvironment()) {
                        try {
                            ICommandHandler<?> handler = RegisterCommand(clazz, plugin);
                            if (handler != null) plugin.GetLogger().info(String.format("Registered command %s", handler.GetName()));
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
    }

    protected abstract ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception;

}
