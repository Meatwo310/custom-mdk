plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

dependencies {
    implementation(libs.mod.publish.plugin)
    implementation(libs.neoforged.moddev.gradle)
    implementation(libs.forgegradle.plugin)
    implementation(libs.fabric.loom)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit"))
}

tasks.processTestResources {
    // Test the current helpers in an isolated build without loading Minecraft plugins.
    from("src/main/kotlin/net/meatwo310/mdk/build") {
        include("SourceSetArtifacts.kt", "ConfigSourceSets.kt", "ResourceComposition.kt")
        into("resource-composition/helpers")
    }
}

tasks.test {
    useJUnit()
    maxParallelForks = 1
}
