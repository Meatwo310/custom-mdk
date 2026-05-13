package net.meatwo310.examplemod;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ServerConfig;
import net.neoforged.fml.config.ModConfig;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.id("26.1.2-fabric"));
        ConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
