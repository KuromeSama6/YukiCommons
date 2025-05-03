package moe.protasis.yukicommons.bungeecord.impl.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.bungeecord.impl.YukiCommonsBungee;
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
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher = new CommandDispatcher<>();

    @Override
    protected ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        if (!(plugin instanceof Plugin))
            throw new IllegalArgumentException(String.format("IAbstractPlugin (%s) passed in must be an inheritor of Plugin!", plugin.getClass()));

        ICommandHandler<?> handler = (ICommandHandler<?>)clazz.getDeclaredConstructor().newInstance();
        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin)plugin, new BungeecordCommandHandler(handler));
        return handler;
    }

    @Override
    protected IBrigadierCommand RegisterBrigadierCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        var handler = (IBrigadierCommand)clazz.getDeclaredConstructor().newInstance();

        LiteralArgumentBuilder<IAbstractCommandExecutor> builder = LiteralArgumentBuilder.literal(handler.GetName());
        builder.requires(c -> handler.GetPermission() == null || c.HasPermission(handler.GetPermission()));
        handler.Build(builder);

        dispatcher.register(builder);

        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin)plugin, new BungeecordBrigadierCommandHandler(handler, dispatcher));

        return handler;
    }
}
