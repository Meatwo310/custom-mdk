import net.meatwo310.mdk.build.PlatformArtifactsExtension
import net.meatwo310.mdk.build.VersionCatalogLibrary
import net.meatwo310.mdk.build.developmentModSourceSet
import net.meatwo310.mdk.build.library
import net.meatwo310.mdk.build.module
import net.meatwo310.mdk.build.versionCatalog

plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom-remap")
}

extensions.configure<PlatformArtifactsExtension> {
    mainJarTaskName.set("remapJar")
    sourcesJarTaskName.set("remapSourcesJar")
}

val modId = project.property("modId").toString()
val minecraftVersion = project.property("minecraftVersion").toString()
val parchmentMinecraftVersion = project.property("parchmentMinecraftVersion").toString()
val parchmentMappingsVersion = project.property("parchmentMappingsVersion").toString()

val developmentSourceSet = developmentModSourceSet()

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
            sourceSet(developmentSourceSet)
        }
    }

    runs.configureEach {
        generateRunConfig.set(true)
        preferGradleTask.set(true)
        if (name == "gameTest") {
            jvmArguments.add("-Dfabric.log.level=debug")
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
