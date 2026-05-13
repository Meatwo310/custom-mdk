package net.meatwo310.examplemod.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue EXAMPLE_FEATURE_ENABLED = BUILDER
            .comment("Whether the example server-side feature is enabled.")
            .define("exampleFeatureEnabled", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isExampleFeatureEnabled() {
        return EXAMPLE_FEATURE_ENABLED.get();
    }
}
