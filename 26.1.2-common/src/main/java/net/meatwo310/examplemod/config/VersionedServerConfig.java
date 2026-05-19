package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigEntries;
import net.meatwo310.examplemod.mdk.config.ConfigEntry;
import net.meatwo310.examplemod.mdk.config.ConfigEntryBuilder;

public final class VersionedServerConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntry.BooleanEntry ENABLE_26_1_2_FEATURE =
            BUILDER.comment("Enable a 26.1.2-specific server feature")
                    .define("enable2612Feature", true);

    public static final ConfigEntries ENTRIES = BUILDER.build();

    private VersionedServerConfig() {}
}
