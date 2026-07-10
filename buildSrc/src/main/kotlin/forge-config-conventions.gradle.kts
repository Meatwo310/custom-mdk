import net.meatwo310.mdk.build.*
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType

val minecraftVersion: String by project
val forgeConfigApiPortVersion = project(":$minecraftVersion-common")
    .property("forgeConfigApiPortVersion")
    .toString()

plugins.withId("java-library") {
    val config = configureConfigSourceSet()
    val sharedConfig = sharedConfigSourceSets(minecraftVersion, "forge-config-conventions")

    config.addConfigClasspath(sharedConfig)

    includeConfigOutput(sharedConfig, config)
    val main = extensions.getByType<SourceSetContainer>().named("main").get()
    for (configSourceSet in sharedConfig.including(config)) {
        main.compileClasspath += configSourceSet.output
    }

    // ForgeGradle 7 loads each output directory as a separate mod root in dev runs.
    // Keep all config classes beside the generated metadata and @Mod class so they
    // are visible from the same module at runtime.
    val stageConfigForRuns = tasks.register<Copy>("stageConfigForRuns") {
        dependsOn(tasks.named("classes"))
        from(sharedConfig.including(config).map { it.output })
        into(layout.buildDirectory.dir("classes/java/main"))
    }
    tasks.matching { it.name.startsWith("run") }.configureEach {
        dependsOn(stageConfigForRuns)
    }
}

plugins.withId("net.minecraftforge.gradle") {
    val dependency = "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortForge)}:$forgeConfigApiPortVersion"
    dependencies.add("implementation", dependency) {
        isTransitive = false
    }
    dependencies.add("ciRuntimeMods", dependency)
}
