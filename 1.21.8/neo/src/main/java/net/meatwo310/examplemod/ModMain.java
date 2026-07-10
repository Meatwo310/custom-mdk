package net.meatwo310.examplemod;

import net.neoforged.fml.common.Mod;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain() {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.21.8-neo"));
    }
}
