package net.meatwo310.examplemod.mdk.config;

public final class ConfigReadableNames {
    private ConfigReadableNames() {
    }

    public static String fromKey(String key) {
        String[] words = key.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        for (int i = 0; i < words.length; i++) {
            words[i] = capitalize(words[i]);
        }

        words = String.join(" ", words).split("[_\\-.]");
        for (int i = 0; i < words.length; i++) {
            words[i] = capitalize(words[i]);
        }

        return String.join(" ", words).replaceAll("\\s++", " ");
    }

    private static String capitalize(String value) {
        if (value.isEmpty()) {
            return value;
        }
        return Character.toTitleCase(value.charAt(0)) + value.substring(1);
    }
}
