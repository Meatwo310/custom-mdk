package net.meatwo310.examplemod;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ServerConfig;
import net.meatwo310.examplemod.config.VersionedConfigSpec;
import net.neoforged.fml.config.ModConfig;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.21.11-fabric"));
        ConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.SERVER, VersionedConfigSpec.bind(ServerConfig.ENTRIES));
    }
}
