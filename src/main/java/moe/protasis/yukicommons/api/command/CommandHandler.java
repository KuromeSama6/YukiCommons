package moe.protasis.yukicommons.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.util.EnvironmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    ExecutionPlatform platform() default ExecutionPlatform.BOTH;

    @AllArgsConstructor
    enum ExecutionPlatform {
        BOTH(null),
        SPIGOT(EnvironmentType.SPIGOT),
        PROXY(EnvironmentType.PROXY);

        @Getter
        private final EnvironmentType environmentType;
    }
}
