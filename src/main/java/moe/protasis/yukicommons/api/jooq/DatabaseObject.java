package moe.protasis.yukicommons.api.jooq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that ALL fields in this class are to be persisted to the database,
 * each field using its field name as its column name and using the default
 * registered serializer. The <code>transient</code> keyword may be used to
 * exclude a field from persistence. Annotating a field with
 * <code>DatabaseField</code>
 * will override this annotation and allow customization of column names and
 * serializers.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseObject {
}
