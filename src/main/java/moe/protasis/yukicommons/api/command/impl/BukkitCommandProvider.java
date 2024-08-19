package moe.protasis.yukicommons.api.command.impl;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.YukiCommons;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
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
        if (command == null) {
            plugin.GetLogger().severe(String.format("Could not register handler for command %s because that command does not Exist! Check if you have it defined in your plugin.yml.", handler.GetName()));
            return null;
        }

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
            IAbstractCommandExecutor executor = IAdapter.Get().AdaptToCommandExecutor(commandSender);
            try {
                JCommander.newBuilder()
                        .addObject(paramter)
                        .build()
                        .parse(strings);
                handler.Handle(executor, paramter);

            } catch (ParameterException e) {
                handler.OnError(executor, e);

            } catch (InvalidCommandException e) {
              commandSender.sendMessage(e.getMessage());

            } catch (PermissionDeniedException e) {
                commandSender.sendMessage("Permission denied");

                YukiCommons.getInstance().getLogger().warning(String.format("Permission denied for %s whilst executing command %s %s:", commandSender.getName(),
                        s, Arrays.toString(strings)));
                e.printStackTrace();

            } catch (CommandExecutionException e) {
                commandSender.sendMessage("The command was not executed correctly.");
                YukiCommons.getInstance().getLogger().severe(String.format("Error for %s whilst executing command %s %s:", commandSender.getName(),
                        s, Arrays.toString(strings)));
                e.printStackTrace();
            }

            return true;
        }
    }
}
