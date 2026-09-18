package net.meatwo310.examplemod.mdk.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ConfigScreenTranslations {
    private ConfigScreenTranslations() {
    }

    public static String getTranslationKey(String modId, ModConfigSpec modSpec, Map<String, Object> valueSpecs, List<String> keyList, String key) {
        Object valueSpec = valueSpecs.get(key);
        String result = valueSpec instanceof ValueSpec spec ? spec.getTranslationKey() : modSpec.getLevelTranslationKey(makeKeyList(keyList, key));
        return result != null ? result : modId + ".configuration." + key;
    }

    private static ArrayList<String> makeKeyList(List<String> keyList, String key) {
        ArrayList<String> result = new ArrayList<>(keyList);
        result.add(key);
        return result;
    }
}
