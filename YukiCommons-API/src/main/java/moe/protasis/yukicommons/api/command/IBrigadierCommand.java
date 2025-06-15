package moe.protasis.yukicommons.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import moe.protasis.yukicommons.api.exception.command.PermissionDeniedException;

public interface IBrigadierCommand {
    void Build(LiteralArgumentBuilder<IAbstractCommandExecutor> builder);
    String GetName();
    String[] GetAliases();
    String GetPermission();

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
}
