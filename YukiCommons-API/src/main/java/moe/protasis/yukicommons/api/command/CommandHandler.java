package moe.protasis.yukicommons.api.command;

import lombok.NonNull;
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
    @NonNull ExecutionPlatform platform() default ExecutionPlatform.BOTH;

    enum ExecutionPlatform {
        BOTH((EnvironmentType[])null),
        SPIGOT(EnvironmentType.BUKKIT),
        PROXY(EnvironmentType.BUNGEECORD);

        private final EnvironmentType[] supportedEnvironments;

        ExecutionPlatform(EnvironmentType... supportedEnvironments) {
            this.supportedEnvironments = supportedEnvironments;
        }

        public boolean IsSupported(EnvironmentType environment) {
            if (supportedEnvironments == null) {
                return true;
            }
            for (EnvironmentType supportedEnvironment : supportedEnvironments) {
                if (supportedEnvironment == environment) {
                    return true;
                }
            }
            return false;
        }
    }
}
