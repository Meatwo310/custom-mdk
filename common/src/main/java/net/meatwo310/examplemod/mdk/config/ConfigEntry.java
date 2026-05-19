package net.meatwo310.examplemod.mdk.config;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ConfigEntry<T> implements Supplier<T>, ConfigElement {
    private final String key, comment;
    private final T defaultValue;
    private Supplier<T> value;

    protected ConfigEntry(String key, String comment, T defaultValue) {
        this.key = key;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.value = () -> defaultValue;
    }

    public String key() {
        return key;
    }

    public String comment() {
        return comment;
    }

    public T get() {
        return value.get();
    }

    public void bind(Supplier<T> supplier) {
        this.value = supplier;
    }

    public T defaultValue() {
        return defaultValue;
    }

    @Override
    public abstract void bindTo(ConfigVisitor visitor);

    public abstract static class RangedValueEntry<T extends Comparable<T>> extends ConfigEntry<T> {
        private final Range<T> range;

        protected RangedValueEntry(String key, String comment, T defaultValue, Range<T> range) {
            super(key, comment, defaultValue);
            this.range = range;
        }

        public Range<T> range() {
            return range;
        }

    }

    public static class IntEntry extends RangedValueEntry<Integer> implements IntSupplier {
        public IntEntry(String key, String comment, int defaultValue, int min, int max) {
            super(key, comment, defaultValue, new Range<>(min, max));
        }

        @Override
        public int getAsInt() {
            return get();
        }

        public void bind(IntSupplier supplier) {
            super.bind(supplier::getAsInt);
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class LongEntry extends RangedValueEntry<Long> implements LongSupplier {
        public LongEntry(String key, String comment, long defaultValue, long min, long max) {
            super(key, comment, defaultValue, new Range<>(min, max));
        }

        @Override
        public long getAsLong() {
            return get();
        }

        public void bind(LongSupplier supplier) {
            super.bind(supplier::getAsLong);
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class DoubleEntry extends RangedValueEntry<Double> implements DoubleSupplier {
        public DoubleEntry(String key, String comment, double defaultValue, double min, double max) {
            super(key, comment, defaultValue, new Range<>(min, max));
        }

        @Override
        public double getAsDouble() {
            return get();
        }

        public void bind(DoubleSupplier supplier) {
            super.bind(supplier::getAsDouble);
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class BooleanEntry extends ConfigEntry<Boolean> implements BooleanSupplier {
        public BooleanEntry(String key, boolean defaultValue, String comment) {
            super(key, comment, defaultValue);
        }

        @Override
        public boolean getAsBoolean() {
            return get();
        }

        public void bind(BooleanSupplier supplier) {
            super.bind(supplier::getAsBoolean);
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class StringEntry extends ConfigEntry<String> {
        public StringEntry(String key, String comment, String defaultValue) {
            super(key, comment, defaultValue);
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class ListEntry<T> extends ConfigEntry<List<T>> {
        private final Supplier<T> newElementSupplier;
        private final Predicate<Object> elementValidator;

        public ListEntry(
                String key,
                String comment,
                List<T> defaultValue,
                Supplier<T> newElementSupplier,
                Predicate<Object> elementValidator) {
            super(key, comment, List.copyOf(defaultValue));
            this.newElementSupplier = newElementSupplier;
            this.elementValidator = elementValidator;
        }

        public Supplier<T> newElementSupplier() {
            return newElementSupplier;
        }

        public Predicate<Object> elementValidator() {
            return elementValidator;
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public static class EnumEntry<E extends Enum<E>> extends ConfigEntry<E> {
        private final Class<E> enumClass;

        public EnumEntry(String key, String comment, E defaultValue, Class<E> enumClass) {
            super(key, comment, defaultValue);
            this.enumClass = enumClass;
        }

        public Class<E> enumClass() {
            return enumClass;
        }

        @Override
        public void bindTo(ConfigVisitor visitor) {
            visitor.bind(this);
        }
    }

    public record Range<T extends Comparable<T>>(T min, T max) {}
}
