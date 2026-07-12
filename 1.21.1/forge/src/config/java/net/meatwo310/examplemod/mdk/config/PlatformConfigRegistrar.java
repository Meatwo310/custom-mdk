package net.meatwo310.examplemod.mdk.config;

import fuzs.forgeconfigapiport.forge.api.neoforge.v4.NeoForgeConfigRegistry;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public final class PlatformConfigRegistrar {
    private PlatformConfigRegistrar() {}

    public static void registerAll(ModContainer modContainer, List<VersionedConfigSpec.BoundConfig> configs) {
        for (var config : configs) {
            config.optionalFileName().ifPresentOrElse(
                    fileName -> NeoForgeConfigRegistry.INSTANCE.register(modContainer, toType(config.side()), config.spec(), fileName),
                    () -> NeoForgeConfigRegistry.INSTANCE.register(modContainer, toType(config.side()), config.spec()));
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
