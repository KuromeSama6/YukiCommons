package moe.protasis.yukicommons.bukkit.impl.command;

import com.beust.jcommander.ParameterException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.InvalidCommandException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class BukkitBrigadierCommandHandler implements CommandExecutor, TabCompleter {
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher;
    private final IBrigadierCommand cmd;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        var executor = YukiCommonsBukkit.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);

        String str;
        if (strings.length == 0) {
            str = command.getLabel();
        } else {
            str = "%s %s".formatted(command.getLabel(), String.join(" ", strings));
        }

        try {
            dispatcher.execute(str, executor);
            return true;

        } catch (CommandSyntaxException e) {
            commandSender.sendMessage("There was an error executing the command: %s".formatted(e.getMessage()));
            commandSender.sendMessage(ChatColor.DARK_AQUA + "Usage:");
            dispatcher.getSmartUsage(dispatcher.getRoot().getChild(cmd.GetName()), executor).values().forEach(c -> commandSender.sendMessage(ChatColor.GRAY + "- " + c));

        } catch (OperationNotPermittedException e) {
            commandSender.sendMessage("Operation not permitted");

        } catch (PermissionDeniedException e) {
            if (e.getMessage() == null) {
                commandSender.sendMessage("%s: Permission denied".formatted(s));
            } else {
                commandSender.sendMessage("%s: Permission denied: %s".formatted(s, e.getMessage()));
            }

//            YukiCommonsBukkit.getInstance().getLogger().warning(String.format("Permission denied for %s whilst executing command %s %s:", commandSender.getName(),
//                    s, Arrays.toString(strings)));

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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        var executor = YukiCommonsBukkit.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);
        String str;
        if (strings.length == 0) {
            str = s;
        } else {
            str = "%s %s".formatted(s, String.join(" ", strings));
        }

        var parseResult = dispatcher.parse(str, executor);

        try {
            return dispatcher.getCompletionSuggestions(parseResult).get().getList().stream()
                    .map(Suggestion::getText)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
