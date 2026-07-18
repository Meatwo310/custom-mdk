package net.meatwo310.examplemod.mdk.config;

public interface ConfigVisitor {
    void translation(String translationKey);

    void worldRestart();

    void gameRestart();

    void push(String key, String comment);

    void pop();

    void pop(int count);

    void bind(ConfigEntry.IntEntry entry);

    void bind(ConfigEntry.LongEntry entry);

    void bind(ConfigEntry.DoubleEntry entry);

    void bind(ConfigEntry.BooleanEntry entry);

    void bind(ConfigEntry.StringEntry entry);

    <T> void bind(ConfigEntry.ListEntry<T> entry);

    <E extends Enum<E>> void bind(ConfigEntry.EnumEntry<E> entry);
}
