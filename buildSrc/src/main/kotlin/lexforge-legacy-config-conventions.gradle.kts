import net.meatwo310.mdk.build.*

val minecraftVersion: String by project

plugins.withId("java-library") {
    val config = configureConfigSourceSet()
    val sharedConfig = sharedConfigSourceSetsWithOptionalVersion(minecraftVersion, "lexforge-legacy-config-conventions")

    config.addConfigClasspath(sharedConfig)

    includeConfigOutput(sharedConfig, config)
    addConfigOutputTo(SourceSet.MAIN_SOURCE_SET_NAME, sharedConfig, config)
}
