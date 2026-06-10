import net.meatwo310.mdk.build.VersionCatalogLibrary
import net.meatwo310.mdk.build.library
import net.meatwo310.mdk.build.module
import net.meatwo310.mdk.build.versionCatalog

plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom-remap")
}

val modId: String by project
val minecraftVersion: String by project
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
        if (name == "gameTest") {
            vmArg("-Dfabric.log.level=debug")
        }
    }
}

dependencies {
    minecraft("${versionCatalog.module(VersionCatalogLibrary.Minecraft)}:$minecraftVersion")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("${versionCatalog.module(VersionCatalogLibrary.ParchmentData)}-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
    })
    modImplementation(versionCatalog.library(VersionCatalogLibrary.FabricLoader))
}
