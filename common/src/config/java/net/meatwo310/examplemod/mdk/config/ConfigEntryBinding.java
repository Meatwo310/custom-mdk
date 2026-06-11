package net.meatwo310.examplemod.mdk.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ConfigEntryBinding<T> extends Supplier<T> {
    void set(T value);

    void flush();

    default void setAndFlush(T value) {
        set(value);
        flush();
    }

    static <T> ConfigEntryBinding<T> of(Supplier<T> getter, Consumer<T> setter, Runnable flusher) {
        return new ConfigEntryBinding<>() {
            @Override
            public T get() {
                return getter.get();
            }

            @Override
            public void set(T value) {
                setter.accept(value);
            }

            @Override
            public void flush() {
                flusher.run();
            }
        };
    }

    static <T> ConfigEntryBinding<T> readOnly(Supplier<T> getter) {
        return new ConfigEntryBinding<>() {
            @Override
            public T get() {
                return getter.get();
            }

            @Override
            public void set(T value) {
                throw new UnsupportedOperationException("Config entry binding is read-only");
            }

            @Override
            public void flush() {
                throw new UnsupportedOperationException("Config entry binding is read-only");
            }
        };
    }

    static <T> ConfigEntryBinding<T> unbound(T defaultValue) {
        return new ConfigEntryBinding<>() {
            @Override
            public T get() {
                return defaultValue;
            }

            @Override
            public void set(T value) {
                throw new IllegalStateException("Config entry is not bound");
            }

            @Override
            public void flush() {
                throw new IllegalStateException("Config entry is not bound");
            }
        };
    }
}
