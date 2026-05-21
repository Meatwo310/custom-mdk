import net.meatwo310.mdk.build.*

val minecraftVersion: String by project
val forgeConfigApiPortVersion: String by project

plugins.withId("fabric-mod-conventions") {
    extensions.configure<FabricModMetadataExtension>("fabricModMetadata") {
        depend("forgeconfigapiport", ">=$forgeConfigApiPortVersion")
    }
}

plugins.withId("java-library") {
    val config = configureConfigSourceSet()
    val configClient = configureConfigSourceSet(CONFIG_CLIENT_SOURCE_SET_NAME)
    val sourceSets = extensions.getByType<SourceSetContainer>()
    val sharedConfig = sharedConfigSourceSets(minecraftVersion, "fabric-config-conventions")
    val client = sourceSets.findByName("client")

    config.addConfigClasspath(sharedConfig)
    configClient.addConfigClasspath(sharedConfig, config)
    if (client != null) {
        configClient.addCompileRuntimeClasspathFrom(client)
    }

    includeConfigOutput(sharedConfig, config, configClient)

    addConfigOutputTo(SourceSet.MAIN_SOURCE_SET_NAME, sharedConfig, config)
    addConfigOutputTo("client", sharedConfig, config, configClient)

    dependencies.add("runtimeOnly", files(config.output))
    dependencies.add("runtimeOnly", files(configClient.output))
}

plugins.withId("net.fabricmc.fabric-loom") {
    val dependency = "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion"
    dependencies.add("implementation", dependency)
    dependencies.add("runtimeMods", dependency)
}

plugins.withId("net.fabricmc.fabric-loom-remap") {
    val dependency = "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion"
    dependencies.add("modImplementation", dependency)
    dependencies.add("runtimeMods", dependency)
}
