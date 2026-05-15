package net.meatwo310.mdk.config;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class ConfigEntries implements Iterable<ConfigEntry<?>> {
    private final List<ConfigEntry<?>> entries;

    ConfigEntries(List<ConfigEntry<?>> entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull Iterator<ConfigEntry<?>> iterator() {
        return entries.iterator();
    }
}
