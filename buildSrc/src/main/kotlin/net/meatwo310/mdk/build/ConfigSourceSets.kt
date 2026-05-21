package net.meatwo310.mdk.build

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

const val CONFIG_SOURCE_SET_NAME = "config"
const val CONFIG_CLIENT_SOURCE_SET_NAME = "configClient"

data class SharedConfigSourceSets(
    val common: SourceSet,
    val version: SourceSet?,
) {
    val all: List<SourceSet> = listOfNotNull(common, version)

    fun including(vararg sourceSets: SourceSet): Array<SourceSet> =
        (all + sourceSets).toTypedArray()
}

fun Project.configureConfigSourceSet(name: String = CONFIG_SOURCE_SET_NAME): SourceSet {
    val sourceSets = extensions.getByType<SourceSetContainer>()
    val sourceSet = sourceSets.findByName(name) ?: sourceSets.create(name) {
        java.srcDir("src/$name/java")
        resources.srcDir("src/$name/resources")
    }
    sourceSet.compileClasspath += configurations.getByName("compileClasspath")
    sourceSet.runtimeClasspath += sourceSet.output + sourceSet.compileClasspath
    return sourceSet
}

fun Project.includeConfigOutput(vararg sourceSets: SourceSet) {
    tasks.named<Jar>("jar") {
        for (sourceSet in sourceSets) {
            from(sourceSet.output)
        }
    }
}

fun Project.includeConfigOutput(sharedConfig: SharedConfigSourceSets, vararg sourceSets: SourceSet) {
    includeConfigOutput(*sharedConfig.including(*sourceSets))
}

fun Project.addConfigOutputTo(sourceSetName: String, vararg configSourceSets: SourceSet) {
    val sourceSets = extensions.getByType<SourceSetContainer>()
    sourceSets.findByName(sourceSetName)?.let { target ->
        for (configSourceSet in configSourceSets) {
            target.compileClasspath += configSourceSet.output
            target.runtimeClasspath += configSourceSet.output
        }
    }
}

fun Project.addConfigOutputTo(
    sourceSetName: String,
    sharedConfig: SharedConfigSourceSets,
    vararg configSourceSets: SourceSet,
) {
    addConfigOutputTo(sourceSetName, *sharedConfig.including(*configSourceSets))
}

fun Project.configSourceSetOrNull(name: String = CONFIG_SOURCE_SET_NAME): SourceSet? =
    extensions.getByType<SourceSetContainer>().findByName(name)

fun Project.requireConfigSourceSet(
    projectPath: String,
    conventionName: String,
    requiredConventionName: String = "a config convention",
): SourceSet {
    evaluationDependsOn(projectPath)
    return project(projectPath).configSourceSetOrNull()
        ?: error("Project '$projectPath' must apply $requiredConventionName before $path can apply $conventionName.")
}

fun Project.sharedConfigSourceSets(minecraftVersion: String, conventionName: String): SharedConfigSourceSets {
    val common = requireConfigSourceSet(":common", conventionName)
    val versionProjectPath = ":$minecraftVersion-common"
    val version = requireConfigSourceSet(versionProjectPath, conventionName)
    return SharedConfigSourceSets(common, version)
}

fun Project.sharedConfigSourceSetsWithOptionalVersion(
    minecraftVersion: String,
    conventionName: String,
): SharedConfigSourceSets {
    val common = requireConfigSourceSet(":common", conventionName)
    val versionProjectPath = ":$minecraftVersion-common"
    evaluationDependsOn(versionProjectPath)
    val version = project(versionProjectPath).configSourceSetOrNull()
    return SharedConfigSourceSets(common, version)
}

fun Project.configureVersionCommonConfigSourceSet(conventionName: String): SourceSet {
    val config = configureConfigSourceSet()
    config.addConfigClasspath(requireConfigSourceSet(":common", conventionName, "common-config-conventions"))
    includeConfigOutput(config)
    return config
}

fun SourceSet.addConfigClasspath(vararg sourceSets: SourceSet) {
    for (sourceSet in sourceSets) {
        compileClasspath += sourceSet.output
        runtimeClasspath += sourceSet.output
    }
}

fun SourceSet.addConfigClasspath(sharedConfig: SharedConfigSourceSets, vararg sourceSets: SourceSet) {
    addConfigClasspath(*sharedConfig.including(*sourceSets))
}

fun SourceSet.addCompileRuntimeClasspathFrom(sourceSet: SourceSet) {
    compileClasspath += sourceSet.compileClasspath
    runtimeClasspath += sourceSet.runtimeClasspath
}
