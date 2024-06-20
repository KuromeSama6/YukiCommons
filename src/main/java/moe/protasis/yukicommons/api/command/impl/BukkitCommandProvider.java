package moe.protasis.yukicommons.api.command.impl;

import com.beust.jcommander.JCommander;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BukkitCommandProvider extends CommandProvider {
    @Override
    protected ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        if (!(plugin instanceof JavaPlugin))
            throw new IllegalArgumentException(String.format("IAbstractPlugin (%s) passed in must be an inheritor of JavaPlugin!", plugin.getClass()));

        ICommandHandler<?> handler = (ICommandHandler<?>)clazz.getDeclaredConstructor().newInstance();
        PluginCommand command = ((JavaPlugin)plugin).getCommand(handler.GetName());
        command.setAliases(Arrays.asList(handler.GetAliases()));
        command.setExecutor(new Handler(handler));

        return handler;
    }

    @AllArgsConstructor
    private static class Handler implements CommandExecutor {
        private final ICommandHandler<?> handler;

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Object paramter = handler.CreateParameterObject();
            JCommander.newBuilder()
                    .addObject(paramter)
                    .build()
                    .parse(strings);

            handler.Handle(IAdapter.Get().AdaptToCommandExecutor(commandSender), paramter);
            return true;
        }
    }
}
