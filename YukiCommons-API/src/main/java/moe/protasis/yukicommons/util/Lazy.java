package moe.protasis.yukicommons.util;

import lombok.Getter;

import java.util.function.Supplier;

public class Lazy<T> {
    private T value;
    private final Supplier<T> supplier;
    @Getter
    private boolean initialized;

    public Lazy(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (!initialized) {
            value = supplier.get();
            initialized = true;
        }
        return value;
    }

}
