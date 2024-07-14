package moe.protasis.yukicommons.api.command;

import com.beust.jcommander.ParameterException;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface ICommandHandler<T> {
    void HandleInternal(IAbstractCommandExecutor executor, T args);
    String GetName();
    String[] GetAliases();

    default String GetPermission() {
        return null;
    }
    default void Handle(IAbstractCommandExecutor executor, Object args) {
        HandleInternal(executor, (T)args);
    }
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
