package net.meatwo310.examplemod;

import net.fabricmc.api.ModInitializer;
import net.meatwo310.examplemod.config.*;
import net.meatwo310.examplemod.mdk.config.ConfigDeclarations;
import net.meatwo310.examplemod.mdk.config.PlatformConfigRegistrar;
import net.meatwo310.examplemod.mdk.config.VersionedConfigSpec;

public class ModMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.id("26.1.2-fabric"));
        var configs = ConfigDeclarations.append(VersionedModConfigs.ALL, ModConfigs.SERVER, FabricServerConfig.ENTRIES);
        PlatformConfigRegistrar.registerAll(Constants.MODID, VersionedConfigSpec.bindAll(configs));
    }
}
