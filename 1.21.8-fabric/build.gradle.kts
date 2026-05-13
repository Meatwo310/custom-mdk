plugins {
    id("fabric-loom-remap-mod-conventions")
}

// Mod Dependencies
dependencies {
    modImplementation(libs.fcap.fabric.mc1218)
    runtimeMods(libs.fcap.fabric.mc1218)
}
