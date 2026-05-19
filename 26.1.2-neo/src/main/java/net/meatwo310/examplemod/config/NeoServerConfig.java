package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigEntries;
import net.meatwo310.examplemod.mdk.config.ConfigEntry;
import net.meatwo310.examplemod.mdk.config.ConfigEntryBuilder;

public final class NeoServerConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntry.BooleanEntry ENABLE_NEO_FEATURE =
            BUILDER.comment("Enable a NeoForge-specific server feature")
                    .define("enableNeoFeature", true);

    public static final ConfigEntries ENTRIES = BUILDER.build();

    private NeoServerConfig() {}
}
