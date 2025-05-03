package moe.protasis.yukicommons.bukkit.impl.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.ICommandHandler;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import org.apache.tools.ant.types.Commandline;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@AllArgsConstructor
public class BukkitCommandHandler implements CommandExecutor {
    private final ICommandHandler<?> handler;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Object parameter = handler.CreateParameterObject();
        IAbstractCommandExecutor executor = YukiCommonsBukkit.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);
        try {
            JCommander.newBuilder()
                    .expandAtSign(false)
                    .addObject(parameter)
                    .build()
                    .parse(Commandline.translateCommandline(String.join(" ", strings)));
            handler.Handle(executor, parameter);

        } catch (ParameterException e) {
            handler.OnError(executor, e);

        } catch (InvalidCommandException e) {
            commandSender.sendMessage(e.getMessage());

        } catch (OperationNotPermittedException e) {
            commandSender.sendMessage("Operation not permitted");

        } catch (PermissionDeniedException e) {
            if (e.getMessage() == null) commandSender.sendMessage("%s: Permission denied".formatted(s));
            else commandSender.sendMessage("%s: Permission denied: %s".formatted(s, e.getMessage()));

//            YukiCommonsBukkit.getInstance().getLogger().warning(String.format("Permission denied for %s whilst executing command %s %s:", commandSender.getName(), s, Arrays.toString(strings)));

        } catch (CommandExecutionException e) {
            commandSender.sendMessage("The command was not executed correctly: %s".formatted(e.getMessage()));
            YukiCommonsBukkit.getInstance().getLogger().severe(String.format("Error for %s whilst executing command %s %s:", commandSender.getName(),
                    s, Arrays.toString(strings)));
            e.printStackTrace();
        } catch (Exception e) {
            commandSender.sendMessage("The command was not executed correctly: An internal error occured while executing this command.");
            e.printStackTrace();
        }

        return true;
    }
}