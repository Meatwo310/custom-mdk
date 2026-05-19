package net.meatwo310.examplemod.config;

import net.meatwo310.examplemod.mdk.config.ConfigDeclaration;
import net.meatwo310.examplemod.mdk.config.ConfigSide;

import java.util.List;

public final class ModConfigs {
    public static final ConfigDeclaration SERVER = ConfigDeclaration.of(ConfigSide.SERVER, ServerConfig.ENTRIES);
    public static final ConfigDeclaration CLIENT =
            ConfigDeclaration.of(ConfigSide.CLIENT, ClientConfig.ENTRIES, "examplemod-client.toml");

    public static final List<ConfigDeclaration> ALL = List.of(SERVER, CLIENT);

    private ModConfigs() {}
}
