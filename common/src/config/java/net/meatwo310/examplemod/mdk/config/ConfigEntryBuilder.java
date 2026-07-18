package net.meatwo310.examplemod.mdk.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigEntryBuilder {
    private final List<ConfigElement> elements = new ArrayList<>();
    private String pendingComment = "";

    /**
     * Appends a comment for the next entry or category. Consecutive comments are joined with newlines.
     *
     * @param comment the comment to append
     * @return this builder
     */
    public ConfigEntryBuilder comment(String comment) {
        this.pendingComment = pendingComment.isEmpty() ? comment : pendingComment + "\n" + comment;
        return this;
    }

    /**
     * Sets the exact translation key for the next entry or category.
     *
     * @param translationKey the translation key to use without deriving it from the category path
     * @return this builder
     */
    public ConfigEntryBuilder translation(String translationKey) {
        elements.add(new ConfigInstruction.Translation(translationKey));
        return this;
    }

    /**
     * Marks the next entry as requiring a world restart.
     *
     * @return this builder
     */
    public ConfigEntryBuilder worldRestart() {
        elements.add(new ConfigInstruction.WorldRestart());
        return this;
    }

    /**
     * Marks the next entry as requiring a game restart. This setting is ignored on platforms that use the Forge Config
     * API.
     *
     * @return this builder
     */
    public ConfigEntryBuilder gameRestart() {
        elements.add(new ConfigInstruction.GameRestart());
        return this;
    }

    public ConfigEntry.IntEntry define(String key, int defaultValue) {
        return defineInRange(key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public ConfigEntry.IntEntry defineInRange(String key, int defaultValue, int min, int max) {
        return register(new ConfigEntry.IntEntry(key, pendingComment, defaultValue, min, max));
    }

    public ConfigEntry.LongEntry define(String key, long defaultValue) {
        return defineInRange(key, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public ConfigEntry.LongEntry defineInRange(String key, long defaultValue, long min, long max) {
        return register(new ConfigEntry.LongEntry(key, pendingComment, defaultValue, min, max));
    }

    public ConfigEntry.DoubleEntry define(String key, double defaultValue) {
        return defineInRange(key, defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
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

    public ConfigEntries category(String key, ConfigEntries children) {
        var category = ConfigEntries.category(key, pendingComment, children);
        elements.addAll(category.elements());
        pendingComment = "";
        return category;
    }

    public ConfigEntryBuilder push(String key) {
        elements.add(new ConfigInstruction.Push(key, pendingComment));
        pendingComment = "";
        return this;
    }

    public ConfigEntryBuilder pop() {
        elements.add(new ConfigInstruction.Pop());
        return this;
    }

    /**
     * Closes the requested number of sections.
     *
     * @param count the number of sections to close
     * @return this builder
     */
    public ConfigEntryBuilder pop(int count) {
        elements.add(new ConfigInstruction.PopCount(count));
        return this;
    }

    public ConfigEntries build() {
        return new ConfigEntries(elements);
    }

    private <T extends ConfigEntry<?>> T register(T entry) {
        elements.add(entry);
        pendingComment = "";
        return entry;
    }
}
