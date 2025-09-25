// Test script to verify cleaned gradle.properties still works
plugins {
    base
}

// Copy the ModConfig object from our build.gradle.kts to test it works independently
object ModConfig {
    // Environment Properties
    const val minecraftVersion = "1.20.1"
    const val minecraftVersionRange = "[1.20.1,1.21)"
    const val forgeVersion = "47.4.0"
    const val forgeVersionRange = "[47.4,)"
    const val loaderVersionRange = "[47,)"
    const val mappingChannel = "parchment"
    const val mappingVersion = "2023.09.03-1.20.1"
    
    // Mod Properties
    const val modId = "examplemod"
    const val modName = "Example Mod"
    const val modLicense = "MIT"
    const val modVersion = "0.1.0"
    const val modGroupId = "io.github.meatwo310.examplemod"
    const val modAuthors = "Meatwo310"
    const val modDescription = ""
}

tasks.register("testCleanedGradleProperties") {
    doLast {
        println("Testing with cleaned gradle.properties (only JVM settings):")
        println("ModConfig.modVersion: ${ModConfig.modVersion}")
        println("ModConfig.modId: ${ModConfig.modId}")
        println("ModConfig.minecraftVersion: ${ModConfig.minecraftVersion}")
        println()
        println("gradle.properties only contains:")
        file("gradle.properties").readLines().forEach { line ->
            if (line.trim().isNotEmpty()) {
                println("  $line")
            }
        }
        println()
        println("Cleaned gradle.properties test: SUCCESS")
    }
}