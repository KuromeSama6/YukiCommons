package moe.protasis.yukicommons.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public interface IBrigadierCommand {
    void Build(LiteralArgumentBuilder<IAbstractCommandExecutor> builder);
    String GetName();
    String[] GetAliases();
    String GetPermission();

    static <T> LiteralArgumentBuilder<IAbstractCommandExecutor> Literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    static <T> RequiredArgumentBuilder<IAbstractCommandExecutor, T> Require(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
