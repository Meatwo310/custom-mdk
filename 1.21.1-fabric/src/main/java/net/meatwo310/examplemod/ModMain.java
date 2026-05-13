package net.meatwo310.examplemod;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ServerConfig;
import net.neoforged.fml.config.ModConfig;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.21.1-fabric"));
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
