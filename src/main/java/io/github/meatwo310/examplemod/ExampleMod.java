package io.github.meatwo310.examplemod;

import io.github.meatwo310.examplemod.config.ServerConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";

    public ExampleMod(FMLJavaModLoadingContext ctx) {
//        ctx.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }
}
