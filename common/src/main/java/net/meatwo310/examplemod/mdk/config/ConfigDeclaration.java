package net.meatwo310.examplemod.mdk.config;

import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@NotNullByDefault
public record ConfigDeclaration(ConfigSide side, ConfigEntries entries, @Nullable String fileName) {
    public ConfigDeclaration {
        if (fileName != null) {
            fileName = fileName.trim();
            if (fileName.isEmpty()) {
                fileName = null;
            }
        }
    }

    public static ConfigDeclaration of(ConfigSide side, ConfigEntries entries) {
        return new ConfigDeclaration(side, entries, null);
    }

    public static ConfigDeclaration of(ConfigSide side, ConfigEntries entries, @Nullable String fileName) {
        return new ConfigDeclaration(side, entries, fileName);
    }

    public Optional<String> optionalFileName() {
        return Optional.ofNullable(fileName);
    }

    public ConfigDeclaration append(ConfigEntries entries) {
        return new ConfigDeclaration(side, this.entries.append(entries), fileName);
    }
}
