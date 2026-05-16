plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom-remap")
}

val modId: String by project
val minecraftVersion: String by project
val fabricApiVersion: String by project
val parchmentMinecraftVersion: String by project
val parchmentMappingsVersion: String by project
val forgeConfigApiPortVersion = project.properties["forgeConfigApiPortVersion"]?.toString()

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
    minecraft("${versionCatalog.module(VersionCatalogLibrary.Minecraft)}:$minecraftVersion")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("${versionCatalog.module(VersionCatalogLibrary.ParchmentData)}-$parchmentMinecraftVersion:$parchmentMappingsVersion@zip")
    })
    modImplementation(versionCatalog.library(VersionCatalogLibrary.FabricLoader))
    modImplementation("${versionCatalog.module(VersionCatalogLibrary.FabricApi)}:$fabricApiVersion")
    if (forgeConfigApiPortVersion != null) {
        modImplementation("${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion")
        add("runtimeMods", "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion")
    }
}
