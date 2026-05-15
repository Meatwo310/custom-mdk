package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.ServerConfig;
import net.meatwo310.examplemod.config.VersionedConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.18.2-forge"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, VersionedConfigSpec.bind(ServerConfig.ENTRIES));
    }
}
