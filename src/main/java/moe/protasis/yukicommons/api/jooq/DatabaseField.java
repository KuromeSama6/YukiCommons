package moe.protasis.yukicommons.api.jooq;

import moe.protasis.yukicommons.api.jooq.impl.DefaultValueSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseField {
    String value() default "";
    Class<? extends IValueSerializer<?, ?>> serializer() default DefaultValueSerializer.class;
}
