plugins {
    id("legacyforge-mod-conventions")
}

val configuredVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.configured) { version { require(configuredVersion) } }
}
