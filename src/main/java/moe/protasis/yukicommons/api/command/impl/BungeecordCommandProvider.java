package moe.protasis.yukicommons.api.command.impl;

import com.beust.jcommander.JCommander;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

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
            Object paramter = handler.CreateParameterObject();
            JCommander.newBuilder()
                    .addObject(paramter)
                    .build()
                    .parse(strings);

            handler.Handle(IAdapter.Get().AdaptToCommandExecutor(commandSender), paramter);
        }
    }
}
