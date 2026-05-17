package net.meatwo310.mdk.config;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigEntries implements Iterable<ConfigEntry<?>> {
    private final List<ConfigEntry<?>> entries;

    ConfigEntries(List<ConfigEntry<?>> entries) {
        this.entries = entries;
    }

    public void bindTo(ConfigEntry.BindingVisitor visitor) {
        for (var entry : entries) {
            entry.bindTo(visitor);
        }
    }

    public ConfigEntries append(ConfigEntries other) {
        var mergedEntries = new ArrayList<ConfigEntry<?>>(entries.size() + other.entries.size());
        mergedEntries.addAll(entries);
        mergedEntries.addAll(other.entries);
        return new ConfigEntries(List.copyOf(mergedEntries));
    }

    @Override
    public @NotNull Iterator<ConfigEntry<?>> iterator() {
        return entries.iterator();
    }
}
