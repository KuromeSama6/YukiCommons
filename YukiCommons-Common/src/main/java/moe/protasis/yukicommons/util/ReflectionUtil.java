package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ReflectionUtil {
    public static List<Method> GetAllMethodsRecursive(Class<?> clazz) {
        List<Method> ret = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));

        if (clazz.getSuperclass() != null) {
            ret.addAll(GetAllMethodsRecursive(clazz.getSuperclass()));
        }

        return ret;
    }
}
