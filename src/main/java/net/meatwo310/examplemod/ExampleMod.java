package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.ServerConfig;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";

    public ExampleMod(IEventBus modEventBus, ModContainer modContainer) {
//        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

    /**
     * Utility method to create a {@link Identifier} with the namespace of this mod.
     * @param path Path of the resource. Example: {@code "example_item"}
     * @return {@link Identifier} with the namespace of this mod and the given path. Example: {@code examplemod:example_item}
     */
    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
