plugins {
    id("legacyforge-mod-conventions")
    id("legacyforge-config-conventions")
}

val configuredVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.configured) { version { require(configuredVersion) } }
}
