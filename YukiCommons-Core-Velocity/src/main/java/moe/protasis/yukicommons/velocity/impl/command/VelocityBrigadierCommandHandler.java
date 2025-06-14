package moe.protasis.yukicommons.velocity.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.velocitypowered.api.command.RawCommand;
import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.command.IBrigadierCommand;
import moe.protasis.yukicommons.api.exception.command.CommandExecutionException;
import moe.protasis.yukicommons.api.exception.command.OperationNotPermittedException;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;
import moe.protasis.yukicommons.velocity.impl.YukiCommonsVelocity;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.format.NamedTextColor.DARK_AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

@AllArgsConstructor
public class VelocityBrigadierCommandHandler implements RawCommand {
    private final IBrigadierCommand cmd;
    private final CommandDispatcher<IAbstractCommandExecutor> dispatcher;

    @Override
    public void execute(Invocation invocation) {
        var executor = YukiCommonsVelocity.getInstance().getAdaptor().AdaptToCommandExecutor(invocation.source());
        var args = invocation.arguments();
        String str = cmd.GetName();
        if (!args.isEmpty()) {
            str += " " + args;
        }

        try {
            dispatcher.execute(str, executor);

        } catch (CommandSyntaxException e) {
            executor.SendMessage("There was an error executing the command: %s".formatted(e.getMessage()));
            invocation.source().sendMessage(Component.text("Usage: ").color(DARK_AQUA));
            dispatcher.getSmartUsage(dispatcher.getRoot().getChild(cmd.GetName()), executor).values().forEach(c -> invocation.source().sendMessage(Component.text("- " + c).color(GRAY)));

        } catch (OperationNotPermittedException e) {
            executor.SendMessage("Operation not permitted");

        } catch (PermissionDeniedException e) {
            if (e.getMessage() == null) executor.SendMessage("Permission denied");
            else executor.SendMessage("Permission denied: %s".formatted(e.getMessage()));

//            YukiCommonsVelocity.getInstance().GetLogger().warning(String.format("Permission denied for %s whilst executing command %s:", invocation.source(), str));

        } catch (CommandExecutionException e) {
            executor.SendMessage("The command was not executed correctly: %s".formatted(e.getMessage()));
            YukiCommonsVelocity.getInstance().GetLogger().severe(String.format("Error for %s whilst executing command %s:", invocation.source(), str));
            e.printStackTrace();

        } catch (Exception e) {
            executor.SendMessage("The command was not executed correctly: An internal error occured while executing this command.");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        var executor = YukiCommonsVelocity.getInstance().getAdaptor().AdaptToCommandExecutor(invocation.source());
        var args = invocation.arguments();
        String str = cmd.GetName() + " " + args;

//        System.out.println("alias=[%s] args=[%s] str=[%s]".formatted(invocation.alias(), invocation.arguments(), str));

        var parseResult = dispatcher.parse(str, executor);

//        System.out.println(str);
        try {
            var suggestions = dispatcher.getCompletionSuggestions(parseResult).get();
            return suggestions.getList().stream()
                .map(Suggestion::getText)
                .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return cmd.GetPermission() == null || invocation.source().hasPermission(cmd.GetPermission());
    }
}
