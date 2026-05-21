import net.meatwo310.mdk.build.configureConfigSourceSet
import net.meatwo310.mdk.build.includeConfigOutput

plugins.withId("java-library") {
    val config = configureConfigSourceSet()
    includeConfigOutput(config)
}
