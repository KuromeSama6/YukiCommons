package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.api.jooq.DatabaseField;
import moe.protasis.yukicommons.api.jooq.DatabaseObject;
import moe.protasis.yukicommons.api.jooq.IValueSerializer;
import moe.protasis.yukicommons.api.jooq.impl.DateTimeSerializer;
import moe.protasis.yukicommons.api.jooq.impl.DefaultValueSerializer;
import moe.protasis.yukicommons.api.jooq.impl.UUIDSerializer;
import org.joda.time.DateTime;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.lang.reflect.Modifier;
import java.util.*;

@UtilityClass
public class JooqUtil {
    static {
        RegisterSerializer(UUID.class, new UUIDSerializer());
        RegisterSerializer(DateTime.class, new DateTimeSerializer());
    }

    private static Map<Class<?>, IValueSerializer<?, ?>> valueSerializers = new HashMap<>();

    public static void RegisterSerializer(Class<?> clazz, IValueSerializer<?, ?> serializer) {
        valueSerializers.put(clazz, serializer);
    }

    public static InsertionData PrepareInsertion(Object obj) {
        try {

            List<Field<?>> fields = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            Class<?> clazz = obj.getClass();
            boolean isGlobal = clazz.isAnnotationPresent(DatabaseObject.class);
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(DatabaseObject.class) || (isGlobal && !Modifier.isTransient(field.getModifiers()))) {
                    String name = field.isAnnotationPresent(DatabaseField.class) ?
                            field.getAnnotation(DatabaseField.class).value() : field.getName();

                    IValueSerializer<?, ?> serializer;
                    if (valueSerializers.containsKey(field.getType())) {
                        serializer = valueSerializers.get(field.getType());
                    } else {
                        Class<? extends IValueSerializer<?, ?>> serializerClass = field.isAnnotationPresent(DatabaseField.class) ?
                                field.getAnnotation(DatabaseField.class).serializer() : null;
                        serializer = serializerClass == null ? new DefaultValueSerializer() :
                                serializerClass.getDeclaredConstructor().newInstance();
                    }

                    fields.add(DSL.field(name));
                    values.add(serializer.Serialize(field.get(obj)));
                }
            }

            return new InsertionData(fields.toArray(new Field<?>[0]), values.toArray(new Object[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class InsertionData extends Pair<Field<?>[], Object[]> {
        public InsertionData(Field<?>[] fields, Object[] objects) {
            super(fields, objects);
        }
    }
}
