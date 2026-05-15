package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigEntries;
import net.meatwo310.mdk.config.ConfigEntry;
import net.minecraftforge.common.ForgeConfigSpec;

public final class VersionedConfigSpec {
    private VersionedConfigSpec() {}

    public static ForgeConfigSpec bind(ConfigEntries entries) {
        var builder = new ForgeConfigSpec.Builder();
        for (var entry : entries) {
            builder.comment(entry.comment());
            if (entry instanceof ConfigEntry.IntEntry e) {
                ForgeConfigSpec.ConfigValue<Integer> value;
                if (e.range().isPresent()) {
                    var range = e.range().get();
                    value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                } else {
                    value = builder.define(e.key(), e.defaultValue());
                }
                e.bind(value);
            } else if (entry instanceof ConfigEntry.LongEntry e) {
                ForgeConfigSpec.ConfigValue<Long> value;
                if (e.range().isPresent()) {
                    var range = e.range().get();
                    value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                } else {
                    value = builder.define(e.key(), e.defaultValue());
                }
                e.bind(value);
            } else if (entry instanceof ConfigEntry.DoubleEntry e) {
                ForgeConfigSpec.ConfigValue<Double> value;
                if (e.range().isPresent()) {
                    var range = e.range().get();
                    value = builder.defineInRange(e.key(), e.defaultValue(), range.min(), range.max());
                } else {
                    value = builder.define(e.key(), e.defaultValue());
                }
                e.bind(value);
            } else if (entry instanceof ConfigEntry.BooleanEntry e) {
                var value = builder.define(e.key(), e.defaultValue());
                e.bind(value);
            } else if (entry instanceof ConfigEntry.StringEntry e) {
                var value = builder.define(e.key(), e.defaultValue());
                e.bind(value);
            }
        }
        return builder.build();
    }
}
