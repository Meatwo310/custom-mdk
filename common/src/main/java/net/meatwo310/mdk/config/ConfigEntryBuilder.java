package net.meatwo310.mdk.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigEntryBuilder {
    private final List<ConfigEntry<?>> entries = new ArrayList<>();
    private String pendingComment = "";

    public ConfigEntryBuilder comment(String comment) {
        this.pendingComment = comment;
        return this;
    }

    public ConfigEntry.IntEntry define(String key, int defaultValue) {
        return register(new ConfigEntry.IntEntry(key, pendingComment, defaultValue));
    }

    public ConfigEntry.IntEntry defineInRange(String key, int defaultValue, int min, int max) {
        return register(new ConfigEntry.IntEntry(key, pendingComment, defaultValue, min, max));
    }

    public ConfigEntry.LongEntry define(String key, long defaultValue) {
        return register(new ConfigEntry.LongEntry(key, pendingComment, defaultValue));
    }

    public ConfigEntry.LongEntry defineInRange(String key, long defaultValue, long min, long max) {
        return register(new ConfigEntry.LongEntry(key, pendingComment, defaultValue, min, max));
    }

    public ConfigEntry.DoubleEntry define(String key, double defaultValue) {
        return register(new ConfigEntry.DoubleEntry(key, pendingComment, defaultValue));
    }

    public ConfigEntry.DoubleEntry defineInRange(String key, double defaultValue, double min, double max) {
        return register(new ConfigEntry.DoubleEntry(key, pendingComment, defaultValue, min, max));
    }

    public ConfigEntry.BooleanEntry define(String key, boolean defaultValue) {
        return register(new ConfigEntry.BooleanEntry(key, defaultValue, pendingComment));
    }

    public ConfigEntry.StringEntry define(String key, String defaultValue) {
        return register(new ConfigEntry.StringEntry(key, pendingComment, defaultValue));
    }

    public <T> ConfigEntry.ListEntry<T> defineList(
            String key, List<T> defaultValue, Supplier<T> newElementSupplier, Predicate<Object> elementValidator) {
        return register(new ConfigEntry.ListEntry<>(
                key, pendingComment, defaultValue, newElementSupplier, elementValidator));
    }

    public <E extends Enum<E>> ConfigEntry.EnumEntry<E> defineEnum(String key, E defaultValue) {
        return register(new ConfigEntry.EnumEntry<>(
                key, pendingComment, defaultValue, defaultValue.getDeclaringClass()));
    }

    public ConfigEntries build() {
        return new ConfigEntries(List.copyOf(entries));
    }

    private <T extends ConfigEntry<?>> T register(T entry) {
        entries.add(entry);
        pendingComment = "";
        return entry;
    }
}
