package net.meatwo310.examplemod.mdk.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class VersionedConfigSpec {
    private VersionedConfigSpec() {}

    public static BoundConfig bind(ConfigDeclaration declaration) {
        return new BoundConfig(
                declaration.side(),
                bind(declaration.entries()),
                declaration.fileName());
    }

    public static List<BoundConfig> bindAll(List<ConfigDeclaration> declarations) {
        return declarations.stream()
                .map(VersionedConfigSpec::bind)
                .toList();
    }

    public record BoundConfig(ConfigSide side, ForgeConfigSpec spec, @Nullable String fileName) {
        public Optional<String> optionalFileName() {
            return Optional.ofNullable(fileName);
        }
    }

    public static ForgeConfigSpec bind(ConfigEntries entries) {
        var builder = new ForgeConfigSpec.Builder();
        entries.bindTo(new ConfigEntryBinder(new ForgeAdapter(builder)));
        return builder.build();
    }

    private record ForgeAdapter(ForgeConfigSpec.Builder builder) implements ConfigEntryBinder.Adapter {
        @Override
        public void comment(String comment) {
            builder.comment(comment);
        }

        @Override
        public void push(String key) {
            builder.push(key);
        }

        @Override
        public void pop() {
            builder.pop();
        }

        @Override
        public Supplier<Integer> defineIntInRange(String key, int defaultValue, int min, int max) {
            return builder.defineInRange(key, defaultValue, min, max);
        }

        @Override
        public Supplier<Long> defineLongInRange(String key, long defaultValue, long min, long max) {
            return builder.defineInRange(key, defaultValue, min, max);
        }

        @Override
        public Supplier<Double> defineDoubleInRange(String key, double defaultValue, double min, double max) {
            return builder.defineInRange(key, defaultValue, min, max);
        }

        @Override
        public Supplier<Boolean> defineBoolean(String key, boolean defaultValue) {
            return builder.define(key, defaultValue);
        }

        @Override
        public Supplier<String> defineString(String key, String defaultValue) {
            return builder.define(key, defaultValue);
        }

        @Override
        public <T> Supplier<? extends List<? extends T>> defineList(
                String key, List<T> defaultValue, Supplier<T> newElementSupplier, Predicate<Object> elementValidator) {
            return builder.defineList(key, defaultValue, elementValidator);
        }

        @Override
        public <E extends Enum<E>> Supplier<E> defineEnum(String key, E defaultValue) {
            return builder.defineEnum(key, defaultValue);
        }
    }
}
