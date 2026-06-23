package net.meatwo310.examplemod;

import net.minecraft.resources.ResourceLocation;

public final class ModUtils {
    private ModUtils() {}

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(Constants.MODID, path);
    }
}
