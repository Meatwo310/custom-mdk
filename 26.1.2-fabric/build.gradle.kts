plugins {
    id("fabric-loom-mod-conventions")
}

val modmenuVersion: String by project

// Mod Dependencies
dependencies {
    runtimeOnly(libs.modmenu) { version { require(modmenuVersion) } }
}
