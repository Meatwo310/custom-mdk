package net.meatwo310.examplemod.mdk.config;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.meatwo310.examplemod.mdk.config.ConfigSide;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public final class PlatformConfigRegistrar {
    private PlatformConfigRegistrar() {}

    public static void registerAll(String modId, List<VersionedConfigSpec.BoundConfig> configs) {
        for (var config : configs) {
            config.optionalFileName().ifPresentOrElse(
                    fileName -> ForgeConfigRegistry.INSTANCE.register(modId, toType(config.side()), config.spec(), fileName),
                    () -> ForgeConfigRegistry.INSTANCE.register(modId, toType(config.side()), config.spec()));
        }
    }

    private static ModConfig.Type toType(ConfigSide side) {
        return switch (side) {
            case COMMON -> ModConfig.Type.COMMON;
            case SERVER -> ModConfig.Type.SERVER;
            case CLIENT -> ModConfig.Type.CLIENT;
        };
    }
}
