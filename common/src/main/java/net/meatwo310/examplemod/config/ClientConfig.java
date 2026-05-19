package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigEntries;
import net.meatwo310.examplemod.mdk.config.ConfigEntry;
import net.meatwo310.examplemod.mdk.config.ConfigEntryBuilder;

public final class ClientConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntry.BooleanEntry SHOW_DEBUG_OVERLAY =
            BUILDER.comment("Show extra debug information on client screens")
                    .define("showDebugOverlay", false);

    public static final ConfigEntries ENTRIES = BUILDER.build();

    private ClientConfig() {}
}
