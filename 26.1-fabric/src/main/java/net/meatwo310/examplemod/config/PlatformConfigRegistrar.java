package net.meatwo310.examplemod.config;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.meatwo310.mdk.config.ConfigSide;
import net.neoforged.fml.config.ModConfig;

import java.util.List;

public final class PlatformConfigRegistrar {
    private PlatformConfigRegistrar() {}

    public static void registerAll(String modId, List<VersionedConfigSpec.BoundConfig> configs) {
        for (var config : configs) {
            config.optionalFileName().ifPresentOrElse(
                    fileName -> ConfigRegistry.INSTANCE.register(modId, toType(config.side()), config.spec(), fileName),
                    () -> ConfigRegistry.INSTANCE.register(modId, toType(config.side()), config.spec()));
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
