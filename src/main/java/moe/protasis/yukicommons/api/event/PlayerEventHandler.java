package moe.protasis.yukicommons.api.event;

import moe.protasis.yukicommons.api.command.CommandHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerEventHandler {
    CommandHandler.ExecutionPlatform platform() default CommandHandler.ExecutionPlatform.BOTH;
}
