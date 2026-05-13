package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.ServerConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain(FMLJavaModLoadingContext ctx) {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.20.1-forge"));
        ctx.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
