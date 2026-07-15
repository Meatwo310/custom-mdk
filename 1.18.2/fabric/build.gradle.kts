plugins {
    id("fabric-loom-remap-mod-conventions")
    id("fabric-api-conventions")
    id("fabric-legacy-config-conventions")
}

val fabricApiVersion = project.property("fabricApiVersion").toString()

// Mod Dependencies
dependencies {
    // Fabric Maven publishes this legacy release as a thin aggregator jar.
    add("ciRuntimeMods", "maven.modrinth:fabric-api:$fabricApiVersion")
}
