package net.meatwo310.examplemod.mdk.config;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NotNullByDefault
public final class ConfigDeclarations {
    private ConfigDeclarations() {}

    public static List<ConfigDeclaration> append(
            List<ConfigDeclaration> declarations,
            ConfigDeclaration target,
            ConfigEntries entries
    ) {
        var appended = new ArrayList<ConfigDeclaration>(declarations.size());
        var matchedCount = 0;
        for (var declaration : declarations) {
            if (matches(declaration, target)) {
                appended.add(declaration.append(entries));
                matchedCount++;
            } else {
                appended.add(declaration);
            }
        }
        if (matchedCount == 0) {
            throw new IllegalArgumentException("Target config declaration is not registered");
        }
        if (matchedCount > 1) {
            throw new IllegalArgumentException("Target config declaration matches multiple registered configs");
        }
        return List.copyOf(appended);
    }

    private static boolean matches(ConfigDeclaration declaration, ConfigDeclaration target) {
        return declaration == target
                || declaration.equals(target)
                || declaration.side() == target.side() && Objects.equals(declaration.fileName(), target.fileName());
    }
}
