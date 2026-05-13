plugins {
    id("fabric-loom-mod-conventions")
}

// Mod Dependencies
dependencies {
    implementation(libs.fcap.fabric.mc2612)
    runtimeMods(libs.fcap.fabric.mc2612)
}
