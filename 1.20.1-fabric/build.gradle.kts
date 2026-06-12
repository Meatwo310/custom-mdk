import net.meatwo310.mdk.build.req

plugins {
    id("fabric-loom-remap-mod-conventions")
    id("fabric-api-conventions")
    id("fabric-config-conventions")
}

val forgeconfigscreensVersion: String by project
val modmenuVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.forgeconfigscreens, req(forgeconfigscreensVersion))
    modRuntimeOnly(libs.modmenu, req(modmenuVersion))
}
