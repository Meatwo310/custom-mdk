import net.meatwo310.mdk.build.*

plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom")
}

val modId = project.property("modId").toString()
val minecraftVersion = project.property("minecraftVersion").toString()

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.named("client").get())
            sourceSet(project(commonProject).sourceSets.main.get())
            sourceSet(project(sharedCommonProject).sourceSets.main.get())
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
    implementation(versionCatalog.library(VersionCatalogLibrary.FabricLoader))
}
