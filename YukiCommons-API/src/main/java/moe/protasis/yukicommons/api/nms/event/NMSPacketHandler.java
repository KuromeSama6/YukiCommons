package moe.protasis.yukicommons.api.nms.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NMSPacketHandler {
    NMSPacketDirection direction();
    boolean async() default false;
}
