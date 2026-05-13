plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

dependencies {
    implementation(libs.neoforged.moddev.gradle)
    implementation(libs.fabric.loom)
}
