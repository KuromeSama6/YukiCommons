package moe.protasis.yukicommons.api.command.impl;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import lombok.var;
import moe.protasis.yukicommons.YukiCommonsBungee;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.tools.ant.types.Commandline;

import java.util.Arrays;

public class BungeecordCommandProvider extends CommandProvider {
    @Override
    protected ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        if (!(plugin instanceof Plugin))
            throw new IllegalArgumentException(String.format("IAbstractPlugin (%s) passed in must be an inheritor of Plugin!", plugin.getClass()));

        ICommandHandler<?> handler = (ICommandHandler<?>)clazz.getDeclaredConstructor().newInstance();
        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin)plugin, new Handler(handler));
        return handler;
    }

    private static class Handler extends Command {
        private final ICommandHandler<?> handler;

        public Handler(ICommandHandler<?> handler) {
            super(handler.GetName(), handler.GetPermission(), handler.GetAliases());
            this.handler = handler;
        }

        @Override
        public void execute(CommandSender commandSender, String[] strings) {
            Object parameter = handler.CreateParameterObject();
            IAbstractCommandExecutor executor = IAdapter.Get().AdaptToCommandExecutor(commandSender);

            try {
                var jCommander = JCommander.newBuilder()
                        .expandAtSign(false)
                        .addObject(parameter)
                        .build();

                jCommander.parse(Commandline.translateCommandline(String.join(" ", strings)));
                handler.Handle(executor, parameter);

            } catch (InvalidCommandException e) {
              commandSender.sendMessage(new TextComponent(e.getMessage()));

            } catch (ParameterException e) {
                handler.OnError(executor, e);

            } catch (OperationNotPermittedException e) {
                commandSender.sendMessage(new TextComponent("Operation not permitted"));

            } catch (PermissionDeniedException e) {
                commandSender.sendMessage(new TextComponent("Permission denied"));

                YukiCommonsBungee.getInstance().getLogger().warning(String.format("Permission denied for %s whilst executing command %s:", commandSender.getName(),
                        Arrays.toString(strings)));
                e.printStackTrace();

            } catch (CommandExecutionException e) {
                commandSender.sendMessage(new TextComponent("The command was not executed correctly."));
                YukiCommonsBungee.getInstance().getLogger().severe(String.format("Error for %s whilst executing command %s:", commandSender.getName(),
                        Arrays.toString(strings)));
                e.printStackTrace();

            } catch (Exception e) {
                commandSender.sendMessage(new TextComponent("The command was not executed correctly: An internal error occured while executing this command."));
                e.printStackTrace();
            }
        }
    }
}
