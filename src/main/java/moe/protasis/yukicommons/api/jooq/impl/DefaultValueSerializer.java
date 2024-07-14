package moe.protasis.yukicommons.api.jooq.impl;

import moe.protasis.yukicommons.api.jooq.IValueSerializer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class DefaultValueSerializer implements IValueSerializer<Object, Object> {
    @Override
    public Object SerializeInternal(Field field, Object in) {
        if (field.getType().isEnum()) {
            return in.toString();
        }
        return in;
    }

    @Override
    public Object DeserializeInternal(Field field, Object out) {
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
            return Evaluate(out);
        }

        if (field.getType().isEnum()) {
            String str = (String)out;
            return Enum.valueOf((Class<Enum>)field.getType(), str);
        }
        return out;
    }

    @Override
    public boolean Passthrough() {
        return true;
    }

    private static boolean Evaluate(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }

        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0;
        }

        if (obj instanceof String) {
            return !((String) obj).isEmpty() && !((String)obj).equalsIgnoreCase("false");
        }

        if (obj instanceof Collection) {
            return !((Collection<?>) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return !((Map<?, ?>) obj).isEmpty();
        }

        // Add more checks for other types as needed

        // If the object type is not explicitly handled, consider it as true
        return false;
    }
}
