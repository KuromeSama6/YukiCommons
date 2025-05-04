package moe.protasis.yukicommons.bungeecord.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.bungeecord.impl.YukiCommonsBungee;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;

public class BungeecordBrigadierCommandHandler extends Command implements TabExecutor {
    private final IBrigadierCommand cmd;
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher;

    public BungeecordBrigadierCommandHandler(IBrigadierCommand cmd, CommandDispatcher<IAbstractCommandExecutor> dispatcher) {
        super(cmd.GetName(), cmd.GetPermission(), cmd.GetAliases());
        this.cmd = cmd;
        this.dispatcher = dispatcher;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        var executor = YukiCommonsBungee.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);
        var str = String.join(" ", strings);

        try {
            dispatcher.execute(str, executor);

        } catch (CommandSyntaxException e) {
            commandSender.sendMessage(new TextComponent("There was an error executing the command: %s".formatted(e.getMessage())));
            commandSender.sendMessage(ChatColor.DARK_AQUA + "Usage:");
            dispatcher.getSmartUsage(dispatcher.getRoot().getChild(cmd.GetName()), executor).values().forEach(c -> commandSender.sendMessage(ChatColor.GRAY + "- " + c));

        } catch (OperationNotPermittedException e) {
            commandSender.sendMessage(new TextComponent("Operation not permitted"));

        } catch (PermissionDeniedException e) {
            if (e.getMessage() == null) commandSender.sendMessage(new TextComponent("%s: Permission denied".formatted(strings[0])));
            else commandSender.sendMessage(new TextComponent("%s: Permission denied: %s".formatted(strings[0], e.getMessage())));

//            YukiCommonsBungee.getInstance().getLogger().warning(String.format("Permission denied for %s whilst executing command %s:", commandSender.getName(),
//                    Arrays.toString(strings)));

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

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        var executor = YukiCommonsBungee.getInstance().getAdaptor().AdaptToCommandExecutor(commandSender);
        var str = String.join(" ", strings);
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
