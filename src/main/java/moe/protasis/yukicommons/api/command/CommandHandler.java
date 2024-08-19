package moe.protasis.yukicommons.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.protasis.yukicommons.util.EnvironmentType;

import java.lang.annotation.*;

/**
 * A marker interface to mark a class a command handler.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CommandHandler {
    /**
     * Acquires the platform on which this command handler should operate.
     * When running on a non-matching platform, this command handler will not be
     * registered.
     * 
     * @return The platform.
     */
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
