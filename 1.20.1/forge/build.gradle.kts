import net.meatwo310.mdk.build.req

plugins {
    id("lexforge-legacy-mod-conventions")
    id("lexforge-legacy-config-conventions")
}

val configuredVersion = project.property("configuredVersion").toString()

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.configured, req(configuredVersion))
}
