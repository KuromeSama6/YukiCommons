package moe.protasis.yukicommons.api.command;

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
    default T CreateParameterObject() {
        try {
            ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
            Type argumentType = type.getActualTypeArguments()[0];
            if (!(argumentType instanceof Class<?>))
                throw new IllegalArgumentException(String.format("Command type %s has invalid type arguments", getClass()));
            Class<?> clazz = (Class<?>)argumentType;
            return (T)clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
