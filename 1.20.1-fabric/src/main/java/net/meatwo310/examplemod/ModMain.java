package net.meatwo310.examplemod;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ServerConfig;
import net.meatwo310.examplemod.config.VersionedConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.20.1-fabric"));
        ForgeConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.SERVER, VersionedConfigSpec.bind(ServerConfig.ENTRIES));
    }
}
