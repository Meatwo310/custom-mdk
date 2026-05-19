package net.meatwo310.examplemod;

import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.ModConfigs;
import net.meatwo310.examplemod.mdk.config.PlatformConfigRegistrar;
import net.meatwo310.examplemod.mdk.config.VersionedConfigSpec;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.21.8-fabric"));
        PlatformConfigRegistrar.registerAll(Constants.MODID, VersionedConfigSpec.bindAll(ModConfigs.ALL));
    }
}
