package net.meatwo310.examplemod;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";

    public ExampleMod(FMLJavaModLoadingContext ctx) {
//        ctx.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    /**
     * Utility method to create a {@link ResourceLocation} with the namespace of this mod.
     * @param path Path of the resource. Example: {@code "example_item"}
     * @return {@link ResourceLocation} with the namespace of this mod and the given path. Example: {@code examplemod:example_item}
     */
    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
