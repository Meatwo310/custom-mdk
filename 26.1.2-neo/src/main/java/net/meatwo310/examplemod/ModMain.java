package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.*;
import net.meatwo310.examplemod.mdk.config.ConfigDeclarations;
import net.meatwo310.examplemod.mdk.config.PlatformConfigRegistrar;
import net.meatwo310.examplemod.mdk.config.VersionedConfigSpec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.id("26.1.2-neo"));
        var configs = ConfigDeclarations.append(VersionedModConfigs.ALL, ModConfigs.SERVER, NeoServerConfig.ENTRIES);
        PlatformConfigRegistrar.registerAll(modContainer, VersionedConfigSpec.bindAll(configs));
    }
}
