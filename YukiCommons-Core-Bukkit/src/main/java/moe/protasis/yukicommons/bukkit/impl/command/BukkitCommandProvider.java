package moe.protasis.yukicommons.bukkit.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BukkitCommandProvider extends CommandProvider {
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher = new CommandDispatcher<>();

    @Override
    protected ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        if (!(plugin instanceof JavaPlugin))
            throw new IllegalArgumentException(String.format("IAbstractPlugin (%s) passed in must be an inheritor of JavaPlugin!", plugin.getClass()));

        ICommandHandler<?> handler = (ICommandHandler<?>)clazz.getDeclaredConstructor().newInstance();
        PluginCommand command = ((JavaPlugin)plugin).getCommand(handler.GetName());
        if (command == null) {
            plugin.GetLogger().severe(String.format("Could not register handler for command %s because that command does not Exist! Check if you have it defined in your plugin.yml.", handler.GetName()));
            return null;
        }

        command.setAliases(Arrays.asList(handler.GetAliases()));
        command.setExecutor(new BukkitCommandHandler(handler));

        return handler;
    }

    @Override
    protected IBrigadierCommand RegisterBrigadierCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        var handler = (IBrigadierCommand)clazz.getDeclaredConstructor().newInstance();
        PluginCommand command = ((JavaPlugin)plugin).getCommand(handler.GetName());
        if (command == null) {
            plugin.GetLogger().severe(String.format("Could not register handler for command %s because that command does not Exist! Check if you have it defined in your plugin.yml.", handler.GetName()));
            return null;
        }

        LiteralArgumentBuilder<IAbstractCommandExecutor> builder = LiteralArgumentBuilder.literal(handler.GetName());
        builder.requires(c -> handler.GetPermission() == null || c.HasPermission(handler.GetPermission()));
        handler.Build(builder);

        command.setAliases(Arrays.asList(handler.GetAliases()));
        if (handler.GetPermission() != null) {
            command.setPermission(handler.GetPermission());
        }

        dispatcher.register(builder);

        var commandHandler = new BukkitBrigadierCommandHandler(dispatcher);
        command.setExecutor(commandHandler);
        command.setTabCompleter(commandHandler);

        return handler;
    }


}
