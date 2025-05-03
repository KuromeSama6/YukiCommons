package moe.protasis.yukicommons.velocity.impl.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.velocity.impl.YukiCommonsVelocity;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import net.kyori.adventure.text.Component;
import org.apache.tools.ant.types.Commandline;

public class VelocityCommandProvider extends CommandProvider {
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher = new CommandDispatcher<>();

    @Override
    protected ICommandHandler<?> RegisterCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        ICommandHandler<?> handler = (ICommandHandler<?>)clazz.getDeclaredConstructor().newInstance();
        var mgr = YukiCommonsVelocity.getInstance().getServer().getCommandManager();
        var h = new Handler(mgr, handler, plugin);
        mgr.register(h.meta, h);
        return handler;
    }

    @Override
    protected IBrigadierCommand RegisterBrigadierCommand(Class<?> clazz, IAbstractPlugin plugin) throws Exception {
        var handler = (IBrigadierCommand)clazz.getDeclaredConstructor().newInstance();
        LiteralArgumentBuilder<IAbstractCommandExecutor> builder = LiteralArgumentBuilder.literal(handler.GetName());
        builder.requires(c -> handler.GetPermission() == null || c.HasPermission(handler.GetPermission()));
        handler.Build(builder);

        dispatcher.register(builder);

        var server = YukiCommonsVelocity.getInstance().getServer();
        var meta = server.getCommandManager().metaBuilder(handler.GetName())
                .aliases(handler.GetAliases())
                .plugin(plugin)
                .build();

        server.getCommandManager().register(meta, new VelocityBrigadierCommandHandler(handler, dispatcher));
        return handler;
    }

    private static class Handler implements RawCommand {
        private final ICommandHandler<?> handler;
        private final CommandMeta meta;
        public Handler(CommandManager mgr, ICommandHandler<?> handler, IAbstractPlugin plugin) {
            this.handler = handler;
            meta = mgr.metaBuilder(handler.GetName())
                    .aliases(handler.GetAliases())
                    .plugin(plugin)
                    .build();
        }

        @Override
        public void execute(Invocation invocation) {
            Object parameter = handler.CreateParameterObject();
            IAbstractCommandExecutor executor = YukiCommonsVelocity.getInstance().getAdaptor().AdaptToCommandExecutor(invocation.source());
            var args = invocation.arguments();
            var commandSender = invocation.source();

            try {
                var jCommander = JCommander.newBuilder()
                        .expandAtSign(false)
                        .addObject(parameter)
                        .build();

                jCommander.parse(Commandline.translateCommandline(args));
                handler.Handle(executor, parameter);

            } catch (InvalidCommandException e) {
                commandSender.sendMessage(Component.text(e.getMessage()));

            } catch (ParameterException e) {
                handler.OnError(executor, e);

            } catch (OperationNotPermittedException e) {
                commandSender.sendMessage(Component.text("Operation not permitted"));

            } catch (PermissionDeniedException e) {
                if (e.getMessage() == null) commandSender.sendMessage(Component.text("Permission denied"));
                else commandSender.sendMessage(Component.text("Permission denied: %s".formatted(e.getMessage())));

            } catch (CommandExecutionException e) {
                commandSender.sendMessage(Component.text("The command was not executed correctly: %s".formatted(e.getMessage())));
                YukiCommonsVelocity.getInstance().GetLogger().severe(String.format("Error for %s whilst executing command %s:", commandSender, args));
                e.printStackTrace();

            } catch (Exception e) {
                commandSender.sendMessage(Component.text("The command was not executed correctly: An internal error occured while executing this command."));
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasPermission(Invocation invocation) {
            return handler.GetPermission() == null || invocation.source().hasPermission(handler.GetPermission());
        }
    }
}
