package moe.protasis.yukicommons.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;

import java.util.Collection;

public interface IBrigadierCommand {
    void Build(LiteralArgumentBuilder<IAbstractCommandExecutor> builder);
    String GetName();
    String[] GetAliases();
    default String GetPermission() {
        return null;
    }

    default void EnsurePlayer(CommandContext<IAbstractCommandExecutor> ctx) {
        if (!ctx.getSource().IsPlayer())
            throw new PermissionDeniedException();
    }

    default <T> LiteralArgumentBuilder<IAbstractCommandExecutor> LiteralArgument(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    default <T> RequiredArgumentBuilder<IAbstractCommandExecutor, T> RequireArgument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    default SuggestionProvider<IAbstractCommandExecutor> Suggest(Collection<String> suggestions) {
        return Suggest(suggestions, false);
    }

    default SuggestionProvider<IAbstractCommandExecutor> Suggest(Collection<String> suggestions, boolean caseSensitive) {
        return (ctx, b) -> {
            suggestions.forEach(c -> {
                if (caseSensitive) {
                    if (c.startsWith(b.getRemaining())) b.suggest(c);

                } else {
                    if (c.toLowerCase().startsWith(b.getRemainingLowerCase())) b.suggest(c);
                }
            });
            return b.buildFuture();
        };
    }
}
