package net.meatwo310.examplemod;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ConfigSide;
import net.meatwo310.examplemod.config.ModConfigs;
import net.meatwo310.examplemod.config.VersionedConfigSpec;
import net.neoforged.fml.config.ModConfig;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.id("26.1-fabric"));
        for (var config : VersionedConfigSpec.bindAll(ModConfigs.CONFIGS)) {
            ConfigRegistry.INSTANCE.register(Constants.MODID, typeOf(config.side()), config.spec());
        }
    }

    private static ModConfig.Type typeOf(ConfigSide side) {
        return Enum.valueOf(ModConfig.Type.class, side.name());
    }
}
