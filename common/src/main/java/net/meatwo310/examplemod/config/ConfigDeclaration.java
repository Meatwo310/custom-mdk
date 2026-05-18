package net.meatwo310.examplemod.config;

import net.meatwo310.mdk.config.ConfigEntries;

public record ConfigDeclaration(String id, ConfigSide side, ConfigEntries entries) {
    public ConfigDeclaration withEntries(ConfigEntries entries) {
        return new ConfigDeclaration(id, side, entries);
    }
}
