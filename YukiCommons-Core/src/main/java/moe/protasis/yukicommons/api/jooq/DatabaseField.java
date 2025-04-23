package moe.protasis.yukicommons.api.jooq;

import moe.protasis.yukicommons.api.jooq.impl.DefaultValueSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field is to be persisted in the database. This annotation
 * ignores the <code>transient</code> keyword and always persists to the
 * database.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseField {
    /**
     * @return The column name.
     */
    String value() default "";

    /**
     * A serializer to serialize and deserialize this field. Defaults to the default
     * registered serializer.
     * 
     * @return The serializer.
     */
    Class<? extends IValueSerializer<?, ?>> serializer() default DefaultValueSerializer.class;
}
