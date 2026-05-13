package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.ServerConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.21.1-neo"));
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
