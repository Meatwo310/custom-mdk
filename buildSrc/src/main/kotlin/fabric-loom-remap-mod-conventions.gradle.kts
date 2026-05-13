import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom-remap")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val modId: String by project
val minecraftVersion: String by project
val fabricApiVersion: String by project
val parchmentMinecraftVersion: String by project
val parchmentMappingsVersion: String by project

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.named("client").get())
            sourceSet(project(sharedCommonProject).sourceSets.main.get())
            sourceSet(project(commonProject).sourceSets.main.get())
        }
    }

    runs.configureEach {
        ideConfigGenerated(true)
    }
}

dependencies {
    minecraft("${libs.module("minecraft")}:$minecraftVersion")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("${libs.module("parchment-data")}-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
    })
    modImplementation(libs.library("fabric-loader"))
    modImplementation("${libs.module("fabric-api")}:$fabricApiVersion")
}
