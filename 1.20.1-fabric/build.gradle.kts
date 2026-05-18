plugins {
    id("fabric-loom-remap-mod-conventions")
}

val forgeconfigscreensVersion: String by project
val modmenuVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.forgeconfigscreens) { version { require(forgeconfigscreensVersion) } }
    modRuntimeOnly(libs.modmenu) { version { require(modmenuVersion) } }
}
