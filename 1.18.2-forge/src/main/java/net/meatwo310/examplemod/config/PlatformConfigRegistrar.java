package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public final class PlatformConfigRegistrar {
    private PlatformConfigRegistrar() {}

    public static void registerAll(List<VersionedConfigSpec.BoundConfig> configs) {
        for (var config : configs) {
            config.optionalFileName().ifPresentOrElse(
                    fileName -> ModLoadingContext.get().registerConfig(toType(config.side()), config.spec(), fileName),
                    () -> ModLoadingContext.get().registerConfig(toType(config.side()), config.spec()));
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
