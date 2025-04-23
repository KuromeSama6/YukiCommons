package moe.protasis.yukicommons.api.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Interface for any command handlers to implement.
 * @param <T> The type of parameters used for this handler.
 *
 */
public interface ICommandHandler<T> {
    /**
     * Called when a command is to be handled by this handler.
     * This method is to be implemented by the developer.
     * 
     * @param executor The command executor.
     * @param args     The parameter.
     */
    void HandleInternal(IAbstractCommandExecutor executor, T args);

    /**
     * Gets the name of this handler. This is usually the command itself.
     * 
     * @return The name.
     */
    String GetName();

    /**
     * Gets any aliases of this handler, apart from its name.
     * 
     * @return This command handler's aliases.
     */
    String[] GetAliases();

    /**
     * Gets the permission required in order to execute this command.
     * 
     * @return The permission. <code>null</code> if no permission is required.
     */
    default String GetPermission() {
        return null;
    }

    default void Handle(IAbstractCommandExecutor executor, Object args) {
        HandleInternal(executor, (T) args);
    }

    /**
     * Called when an <code>ParamterException</code> is thrown whilst executing this
     * command, oftentimes caused by one or more illegal paramters.
     * 
     * @param executor The executor of the command.
     * @param e        The exception.
     */
    default void OnError(IAbstractCommandExecutor executor, ParameterException e) {
        executor.SendMessage(String.format("The command was not executed correctly: %s", e.getMessage()));
    }

    default T CreateParameterObject() {
        try {
            // Get the actual type argument T from the handler's class hierarchy
            Class<?> handlerClass = getClass();
            Type type = FindParameterizedType(handlerClass, ICommandHandler.class);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];

                // Create a new instance of T using the default constructor
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true); // In case the constructor is not public
                return constructor.newInstance();
            }
            throw new IllegalArgumentException("Type parameter T is not specified correctly.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of T", e);
        }
    }

    default void SendUsage(IAbstractCommandExecutor executor) {
        var obj = JCommander.newBuilder()
                .addObject(CreateParameterObject())
                .build();
        StringBuilder sb = new StringBuilder();
        obj.usage(sb);
        executor.SendMessage(sb.toString());
    }

    static Type FindParameterizedType(Class<?> clazz, Class<?> targetInterface) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(targetInterface)) {
                return type;
            }
        }

        if (clazz.getSuperclass() != null) {
            return FindParameterizedType(clazz.getSuperclass(), targetInterface);
        }

        throw new IllegalArgumentException("Specified interface not found in class hierarchy.");
    }
}
