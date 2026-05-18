package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigEntries;

import java.util.ArrayList;
import java.util.List;

public final class ModConfigs {
    public static final ConfigDeclaration SERVER = server("server", ServerConfig.ENTRIES);

    public static final List<ConfigDeclaration> CONFIGS = List.of(SERVER);

    private ModConfigs() {}

    public static ConfigDeclaration common(ConfigEntries entries) {
        return common("common", entries);
    }

    public static ConfigDeclaration client(ConfigEntries entries) {
        return client("client", entries);
    }

    public static ConfigDeclaration server(ConfigEntries entries) {
        return server("server", entries);
    }

    public static ConfigDeclaration common(String id, ConfigEntries entries) {
        return new ConfigDeclaration(id, ConfigSide.COMMON, entries);
    }

    public static ConfigDeclaration client(String id, ConfigEntries entries) {
        return new ConfigDeclaration(id, ConfigSide.CLIENT, entries);
    }

    public static ConfigDeclaration server(String id, ConfigEntries entries) {
        return new ConfigDeclaration(id, ConfigSide.SERVER, entries);
    }

    public static List<ConfigDeclaration> replace(ConfigDeclaration declaration, ConfigEntries entries) {
        var configs = new ArrayList<>(CONFIGS);
        for (var i = 0; i < configs.size(); i++) {
            if (configs.get(i).id().equals(declaration.id())) {
                configs.set(i, declaration.withEntries(entries));
                return List.copyOf(configs);
            }
        }
        throw new IllegalArgumentException("No config declared for " + declaration.id());
    }
}
