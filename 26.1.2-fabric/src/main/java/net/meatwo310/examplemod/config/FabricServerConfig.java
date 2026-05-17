package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigEntries;
import net.meatwo310.mdk.config.ConfigEntry;
import net.meatwo310.mdk.config.ConfigEntryBuilder;

public final class FabricServerConfig {
    private static final ConfigEntryBuilder BUILDER = new ConfigEntryBuilder();

    public static final ConfigEntry.BooleanEntry ENABLE_FABRIC_FEATURE =
            BUILDER.comment("Enable a Fabric-specific server feature")
                    .define("enableFabricFeature", true);

    public static final ConfigEntries ENTRIES = VersionedServerConfig.ENTRIES.append(BUILDER.build());

    private FabricServerConfig() {}
}
