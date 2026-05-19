package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigDeclaration;
import net.meatwo310.examplemod.mdk.config.ConfigDeclarations;

import java.util.List;

public final class VersionedModConfigs {
    public static final List<ConfigDeclaration> ALL =
            ConfigDeclarations.append(ModConfigs.ALL, ModConfigs.SERVER, VersionedServerConfig.ENTRIES);

    private VersionedModConfigs() {}
}
