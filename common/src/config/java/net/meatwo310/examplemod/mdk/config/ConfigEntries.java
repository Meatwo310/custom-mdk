package net.meatwo310.examplemod.mdk.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigEntries {
    private final List<ConfigElement> elements;

    ConfigEntries(List<? extends ConfigElement> elements) {
        this.elements = List.copyOf(elements);
    }

    public static ConfigEntries category(String key, String comment, ConfigEntries children) {
        var elements = new ArrayList<ConfigElement>(children.elements.size() + 2);
        elements.add(new ConfigInstruction.Push(key, comment));
        elements.addAll(children.elements);
        elements.add(new ConfigInstruction.Pop());
        return new ConfigEntries(elements);
    }

    public void bindTo(ConfigVisitor visitor) {
        for (var element : elements) {
            element.bindTo(visitor);
        }
    }

    public ConfigEntries append(ConfigEntries other) {
        var mergedElements = new ArrayList<ConfigElement>(elements.size() + other.elements.size());
        mergedElements.addAll(elements);
        mergedElements.addAll(other.elements);
        return new ConfigEntries(mergedElements);
    }

    List<ConfigElement> elements() {
        return elements;
    }
}
