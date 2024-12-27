package moe.protasis.yukicommons.util;

import lombok.RequiredArgsConstructor;
import moe.protasis.yukicommons.api.jooq.DatabaseField;
import moe.protasis.yukicommons.api.jooq.DatabaseObject;
import moe.protasis.yukicommons.api.jooq.IValueSerializer;
import moe.protasis.yukicommons.api.jooq.impl.CUIDSerializer;
import moe.protasis.yukicommons.api.jooq.impl.DateTimeSerializer;
import moe.protasis.yukicommons.api.jooq.impl.DefaultValueSerializer;
import moe.protasis.yukicommons.api.jooq.impl.UUIDSerializer;
import org.joda.time.DateTime;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.lang.reflect.Modifier;
import java.util.*;

@RequiredArgsConstructor
public class JooqHelper {
    private static final Map<Class<?>, IValueSerializer<?, ?>> valueSerializers;
    static {
        valueSerializers = new HashMap<>();
        RegisterSerializer(UUID.class, new UUIDSerializer());
        RegisterSerializer(DateTime.class, new DateTimeSerializer());
        RegisterSerializer(CUID.class, new CUIDSerializer());
    }

    public static void RegisterSerializer(Class<?> clazz, IValueSerializer<?, ?> serializer) {
        valueSerializers.put(clazz, serializer);
    }

    private Data data;

    public InsertValuesStepN<?> PrepareInsertion(Object obj, InsertSetStep<org.jooq.Record> step) {
        data = Prepare(obj);
        return step
                .columns(data.first)
                .values(data.second);
    }

    public SelectSelectStep<?> PrepareSelection(Object obj, DSLContext ctx) {
        data = Prepare(obj);
        return ctx.select(data.first);
    }

    public <T> List<T> GetResults(Result<?> res, Class<T> clazz) {
        List<T> ret = new ArrayList<>();

        try {
            for (org.jooq.Record row : res) {
                Object obj = clazz.getDeclaredConstructor().newInstance();
                boolean isGlobal = clazz.isAnnotationPresent(DatabaseObject.class);

                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(DatabaseObject.class) || (isGlobal && !Modifier.isTransient(field.getModifiers()))) {
                        String name = GetFieldDatabaseName(field);
                        IValueSerializer<?, ?> serializer = GetSerializer(field);
                        field.set(obj, serializer.Deserialize(field, row.get(name)));
                    }
                }

                ret.add((T)obj);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    private Data Prepare(Object obj) {
        try {

            List<Field<?>> fields = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            Class<?> clazz = obj.getClass();
            boolean isGlobal = clazz.isAnnotationPresent(DatabaseObject.class);
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(DatabaseObject.class) || (isGlobal && !Modifier.isTransient(field.getModifiers()))) {
                    String name = GetFieldDatabaseName(field);

                    IValueSerializer<?, ?> serializer = GetSerializer(field);
                    fields.add(DSL.field(name));

                    values.add(serializer.Serialize(field, field.get(obj)));
                }
            }

            return new Data(fields.toArray(new Field<?>[0]), values.toArray(new Object[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String GetFieldDatabaseName(java.lang.reflect.Field field) {
        return field.isAnnotationPresent(DatabaseField.class) ? field.getAnnotation(DatabaseField.class).value() : field.getName();
    }

    private static IValueSerializer<?, ?> GetSerializer(java.lang.reflect.Field field) throws Exception {
        IValueSerializer<?, ?> serializer;
        DefaultValueSerializer def = new DefaultValueSerializer();
        if (field.isAnnotationPresent(DatabaseField.class)) {
            Class<? extends IValueSerializer<?, ?>> serializerClass = field.getAnnotation(DatabaseField.class).serializer();
            if (serializerClass.equals(DefaultValueSerializer.class)) {
                serializer = valueSerializers.getOrDefault(field.getType(), def);
            } else {
                serializer = serializerClass.getDeclaredConstructor().newInstance();
            }

        } else serializer = valueSerializers.getOrDefault(field.getType(), def);
        return serializer;
    }

    private static class Data extends Pair<Field<?>[], Object[]> {

        public Data(Field<?>[] fields, Object[] values) {
            super(fields, values);
        }
    }
}
