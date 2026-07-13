import net.meatwo310.mdk.build.req

plugins {
    id("fabric-loom-remap-mod-conventions")
    id("fabric-api-conventions")
    id("fabric-config-conventions")
}

val forgeconfigscreensVersion = project.property("forgeconfigscreensVersion").toString()
val modmenuVersion = project.property("modmenuVersion").toString()

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.forgeconfigscreens, req(forgeconfigscreensVersion))
    modRuntimeOnly(libs.modmenu, req(modmenuVersion))
}
