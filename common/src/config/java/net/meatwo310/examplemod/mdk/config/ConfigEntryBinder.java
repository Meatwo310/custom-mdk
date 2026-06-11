package net.meatwo310.examplemod.mdk.config;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ConfigEntryBinder implements ConfigVisitor {
    private final Adapter adapter;

    public ConfigEntryBinder(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void bind(ConfigEntry.IntEntry entry) {
        adapter.comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineIntInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.LongEntry entry) {
        adapter.comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineLongInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.DoubleEntry entry) {
        adapter.comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineDoubleInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.BooleanEntry entry) {
        adapter.comment(entry.comment());
        entry.bind(adapter.defineBoolean(entry.key(), entry.defaultValue()));
    }

    @Override
    public void bind(ConfigEntry.StringEntry entry) {
        adapter.comment(entry.comment());
        entry.bind(adapter.defineString(entry.key(), entry.defaultValue()));
    }

    @Override
    public <T> void bind(ConfigEntry.ListEntry<T> entry) {
        adapter.comment(entry.comment());
        var value = adapter.defineList(
                entry.key(), entry.defaultValue(), entry.newElementSupplier(), entry.elementValidator());
        entry.bind(value);
    }

    @Override
    public <E extends Enum<E>> void bind(ConfigEntry.EnumEntry<E> entry) {
        adapter.comment(entry.comment());
        entry.bind(adapter.defineEnum(entry.key(), entry.defaultValue()));
    }

    @Override
    public void push(String key, String comment) {
        adapter.comment(comment);
        adapter.push(key);
    }

    @Override
    public void pop() {
        adapter.pop();
    }

    public interface Adapter {
        void comment(String comment);

        void push(String key);

        void pop();

        ConfigEntryBinding<Integer> defineIntInRange(String key, int defaultValue, int min, int max);

        ConfigEntryBinding<Long> defineLongInRange(String key, long defaultValue, long min, long max);

        ConfigEntryBinding<Double> defineDoubleInRange(String key, double defaultValue, double min, double max);

        ConfigEntryBinding<Boolean> defineBoolean(String key, boolean defaultValue);

        ConfigEntryBinding<String> defineString(String key, String defaultValue);

        <T> ConfigEntryBinding<List<T>> defineList(
                String key, List<T> defaultValue, Supplier<T> newElementSupplier, Predicate<Object> elementValidator);

        <E extends Enum<E>> ConfigEntryBinding<E> defineEnum(String key, E defaultValue);
    }
}
