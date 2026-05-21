plugins {
    id("fabric-loom-remap-mod-conventions")
    id("fabric-config-conventions")
}

val modmenuVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.modmenu) { version { require(modmenuVersion) } }
}
