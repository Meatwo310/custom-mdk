plugins {
    id("fabric-loom-mod-conventions")
}

// Mod Dependencies
dependencies {
    implementation(libs.fcap.fabric.mc261)
    runtimeMods(libs.fcap.fabric.mc261)
}
