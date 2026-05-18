package net.meatwo310.examplemod;

import net.meatwo310.examplemod.config.ConfigSide;
import net.meatwo310.examplemod.config.ModConfigs;
import net.meatwo310.examplemod.config.VersionedConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.18.2-forge"));
        for (var config : VersionedConfigSpec.bindAll(ModConfigs.CONFIGS)) {
            ModLoadingContext.get().registerConfig(typeOf(config.side()), config.spec());
        }
    }

    private static ModConfig.Type typeOf(ConfigSide side) {
        return Enum.valueOf(ModConfig.Type.class, side.name());
    }
}
