package moe.protasis.yukicommons.bungeecord.impl.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.bungeecord.impl.YukiCommonsBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.apache.tools.ant.types.Commandline;

import java.util.Arrays;

public class BungeecordCommandHandler extends Command {
    private final ICommandHandler<?> handler;

    public BungeecordCommandHandler(ICommandHandler<?> handler) {
        super(handler.GetName(), handler.GetPermission(), handler.GetAliases());
        this.handler = handler;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Object parameter = handler.CreateParameterObject();
        IAbstractCommandExecutor executor = YukiCommonsBungee.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);

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
            if (e.getMessage() == null) commandSender.sendMessage(new TextComponent("%s: Permission denied".formatted(strings[0])));
            else commandSender.sendMessage(new TextComponent("%s: Permission denied: %s".formatted(strings[0], e.getMessage())));

        } catch (CommandExecutionException e) {
            commandSender.sendMessage(new TextComponent("The command was not executed correctly: %s".formatted(e.getMessage())));
            YukiCommonsBungee.getInstance().getLogger().severe(String.format("Error for %s whilst executing command %s:", commandSender.getName(),
                    Arrays.toString(strings)));
            e.printStackTrace();

        } catch (Exception e) {
            commandSender.sendMessage(new TextComponent("The command was not executed correctly: An internal error occured while executing this command."));
            e.printStackTrace();
        }
    }
}
