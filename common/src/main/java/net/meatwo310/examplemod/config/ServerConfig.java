package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigEntries;
import net.meatwo310.examplemod.mdk.config.ConfigEntry;
import net.meatwo310.examplemod.mdk.config.ConfigEntryBuilder;

import java.util.List;

public class ServerConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntry.IntEntry SOME_VALUE =
            BUILDER.comment("Some description")
                    .define("someValue", Integer.MAX_VALUE);
    public static final ConfigEntry.IntEntry SOME_RANGED_VALUE =
            BUILDER.comment("Some description")
                    .defineInRange("someRangedValue", 10, 0, Integer.MAX_VALUE);
    public static final ConfigEntry.LongEntry SOME_LONG_VALUE =
            BUILDER.comment("Some long description")
                    .define("someLongValue", 100L);
    public static final ConfigEntry.DoubleEntry SOME_RANGED_DOUBLE_VALUE =
            BUILDER.comment("Some ranged double description")
                    .defineInRange("someRangedDoubleValue", 0.5, 0.0, 1.0);
    public static final ConfigEntry.StringEntry SOME_STRING_VALUE =
            BUILDER.comment("Some string description")
                    .define("someStringValue", "value");
    public static final ConfigEntry.BooleanEntry SOME_FLAG =
            BUILDER.comment("Some flag")
                    .define("someFlag", false);
    public static final ConfigEntry.ListEntry<String> SOME_STRING_LIST =
            BUILDER.comment("Some string list")
                    .defineList("someStringList", List.of("alpha", "beta"), () -> "alpha", value -> value instanceof String);
    public static final ConfigEntry.EnumEntry<Mode> SOME_MODE =
            BUILDER.comment("Some mode")
                    .defineEnum("someMode", Mode.FIRST);
    public static final ConfigEntries CATEGORY1 =
            BUILDER.comment("Category 1")
                    .category("category1", Category1.ENTRIES);

    public static final ConfigEntries ENTRIES = BUILDER.build();

    public static class Category1 {
        private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

        public static final ConfigEntry.IntEntry KEY1 =
                BUILDER.comment("Category 1 key 1")
                        .define("key1", 1);

        public static final ConfigEntries ENTRIES = BUILDER.build();
    }

    public enum Mode {
        FIRST,
        SECOND
    }
}
