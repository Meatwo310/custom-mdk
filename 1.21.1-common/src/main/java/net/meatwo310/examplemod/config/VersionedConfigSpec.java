package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigEntries;
import net.meatwo310.mdk.config.ConfigEntry;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class VersionedConfigSpec {
    private VersionedConfigSpec() {}

    public static ModConfigSpec bind(ConfigEntries entries) {
        var builder = new ModConfigSpec.Builder();
        for (var entry : entries) {
            builder.comment(entry.comment());
            switch (entry) {
                case ConfigEntry.IntEntry e -> {
                    ModConfigSpec.ConfigValue<Integer> value;
                    if (e.range().isPresent()) {
                        var range = e.range().get();
                        value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                    } else {
                        value = builder.define(e.key(), e.defaultValue());
                    }
                    e.bind(value);
                }
                case ConfigEntry.LongEntry e -> {
                    ModConfigSpec.ConfigValue<Long> value;
                    if (e.range().isPresent()) {
                        var range = e.range().get();
                        value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                    } else {
                        value = builder.define(e.key(), e.defaultValue());
                    }
                    e.bind(value);
                }
                case ConfigEntry.DoubleEntry e -> {
                    ModConfigSpec.ConfigValue<Double> value;
                    if (e.range().isPresent()) {
                        var range = e.range().get();
                        value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                    } else {
                        value = builder.define(e.key(), e.defaultValue());
                    }
                    e.bind(value);
                }
                case ConfigEntry.BooleanEntry e -> {
                    var value = builder.define(e.key(), e.defaultValue());
                    e.bind(value);
                }
                case ConfigEntry.StringEntry e -> {
                    var value = builder.define(e.key(), e.defaultValue());
                    e.bind(value);
                }
                case ConfigEntry.ListEntry<?> e -> bindList(builder, e);
                case ConfigEntry.EnumEntry<?> e -> bindEnum(builder, e);
                default -> {
                }
            }
        }
        return builder.build();
    }

    private static <T> void bindList(ModConfigSpec.Builder builder, ConfigEntry.ListEntry<T> entry) {
        var value = builder.defineList(entry.key(), entry.defaultValue(), entry.newElementSupplier(), entry.elementValidator());
        entry.bind(() -> java.util.List.copyOf(value.get()));
    }

    private static <E extends Enum<E>> void bindEnum(ModConfigSpec.Builder builder, ConfigEntry.EnumEntry<E> entry) {
        var value = builder.defineEnum(entry.key(), entry.defaultValue());
        entry.bind(value);
    }
}
