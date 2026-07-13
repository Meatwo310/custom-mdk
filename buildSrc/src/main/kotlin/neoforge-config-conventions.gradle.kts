import net.meatwo310.mdk.build.*

val minecraftVersion = project.property("minecraftVersion").toString()

plugins.withId("java-library") {
    val config = configureConfigSourceSet()
    val configClient = configureConfigSourceSet(CONFIG_CLIENT_SOURCE_SET_NAME)
    val sharedConfig = sharedConfigSourceSets(minecraftVersion, "neoforge-config-conventions")

    config.addConfigClasspath(sharedConfig)
    configClient.addConfigClasspath(sharedConfig, config)
    configClient.addCompileRuntimeClasspathFrom(config)

    includeConfigOutput(sharedConfig, config, configClient)

    addConfigOutputTo(SourceSet.MAIN_SOURCE_SET_NAME, sharedConfig, config, configClient)
}
