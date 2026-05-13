package net.meatwo310.examplemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue EXAMPLE_FEATURE_ENABLED = BUILDER
            .comment("Whether the example server-side feature is enabled.")
            .define("exampleFeatureEnabled", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isExampleFeatureEnabled() {
        return EXAMPLE_FEATURE_ENABLED.get();
    }
}
