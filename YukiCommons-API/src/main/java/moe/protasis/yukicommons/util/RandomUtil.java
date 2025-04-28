package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RandomUtil {
    public static int Range(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static double Range(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static <T> T Choose(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(Range(0, list.size() - 1));
    }

    public static <T> T Choose(T[] array) {
        if (array.length == 0) {
            return null;
        }
        return array[Range(0, array.length - 1)];
    }
}
