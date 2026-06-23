package net.meatwo310.examplemod;

import net.minecraft.resources.Identifier;

public final class ModUtils {
    private ModUtils() {}

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MODID, path);
    }
}
