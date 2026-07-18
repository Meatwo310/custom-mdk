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
        comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineIntInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.LongEntry entry) {
        comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineLongInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.DoubleEntry entry) {
        comment(entry.comment());
        var range = entry.range();
        entry.bind(adapter.defineDoubleInRange(entry.key(), entry.defaultValue(), range.min(), range.max()));
    }

    @Override
    public void bind(ConfigEntry.BooleanEntry entry) {
        comment(entry.comment());
        entry.bind(adapter.defineBoolean(entry.key(), entry.defaultValue()));
    }

    @Override
    public void bind(ConfigEntry.StringEntry entry) {
        comment(entry.comment());
        entry.bind(adapter.defineString(entry.key(), entry.defaultValue()));
    }

    @Override
    public <T> void bind(ConfigEntry.ListEntry<T> entry) {
        comment(entry.comment());
        var value = adapter.defineList(
                entry.key(), entry.defaultValue(), entry.newElementSupplier(), entry.elementValidator());
        entry.bind(value);
    }

    @Override
    public <E extends Enum<E>> void bind(ConfigEntry.EnumEntry<E> entry) {
        comment(entry.comment());
        entry.bind(adapter.defineEnum(entry.key(), entry.defaultValue()));
    }

    @Override
    public void translation(String translationKey) {
        adapter.translation(translationKey);
    }

    @Override
    public void worldRestart() {
        adapter.worldRestart();
    }

    @Override
    public void gameRestart() {
        adapter.gameRestart();
    }

    @Override
    public void push(String key, String comment) {
        comment(comment);
        adapter.push(key);
    }

    @Override
    public void pop() {
        adapter.pop();
    }

    @Override
    public void pop(int count) {
        adapter.pop(count);
    }

    private void comment(String comment) {
        if (!comment.isBlank()) {
            adapter.comment(comment);
        }
    }

    public interface Adapter {
        void comment(String comment);

        void translation(String translationKey);

        void worldRestart();

        default void gameRestart() {}

        void push(String key);

        void pop();

        void pop(int count);

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
