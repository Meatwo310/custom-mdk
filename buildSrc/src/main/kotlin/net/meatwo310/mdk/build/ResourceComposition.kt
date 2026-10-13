package net.meatwo310.mdk.build

import java.io.File
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

/** Replace every original copy of a composed resource, including copies in nested CopySpecs. */
private class ComposedResourceFilter(private val directory: File) : Action<FileCopyDetails> {
    override fun execute(details: FileCopyDetails) {
        val composed = directory.resolve(details.path)
        if (composed.isFile && details.file != composed) {
            details.exclude()
        }
    }
}

/** Raw inputs must not survive resource renames or exclusions in sources jars. */
private class SourceResourceFilter(private val roots: Set<File>) : Action<FileCopyDetails> {
    override fun execute(details: FileCopyDetails) {
        if (roots.any { details.file.toPath().startsWith(it.toPath()) }) {
            details.exclude()
        }
    }
}

private data class ResourceSource(val owner: Project, val sourceSet: SourceSet)

class ResourceComposition internal constructor(private val project: Project) {
    private val sources = mutableListOf<ResourceSource>()
    private val resourceDirectory = project.layout.buildDirectory.dir("mdk/composedResources")
    val resources: TaskProvider<Sync> = project.tasks.register<Sync>("composeModResources") {
        description = "Composes resources by project and source set priority."
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        into(resourceDirectory)
    }
    private var development: SourceSet? = null

    init {
        val filter = ComposedResourceFilter(resourceDirectory.get().asFile)
        project.tasks.named<Jar>("jar") {
            from(resources)
            eachFile(filter)
        }
        project.tasks.withType<Jar>().matching { it.name == "sourcesJar" }.configureEach {
            from(resources)
            eachFile(filter)
        }
        project.extensions.getByType<SourceSetContainer>()
            .matching { it.name == "main" || it.name == "client" }.all {
                include(project, this)
            }
        project.afterEvaluate {
            val ordered = sources.sortedWith(compareBy<ResourceSource>(
                { if (it.owner == project) 0 else if (it.owner.path == ":common") 2 else 1 },
                {
                    when (it.sourceSet.name) {
                        "configClient" -> 0
                        "client" -> 1
                        "config" -> 2
                        else -> 3
                    }
                },
            ))
            this@ResourceComposition.resources.configure {
                for ((owner, sourceSet) in ordered) {
                    dependsOn(owner.tasks.named(sourceSet.processResourcesTaskName))
                    from(sourceSet.output.resourcesDir)
                }
            }
            val sourceResourceFilter = SourceResourceFilter(
                sources.flatMap { it.sourceSet.resources.srcDirs }.toSet(),
            )
            project.tasks.withType<Jar>().matching { it.name == "sourcesJar" }.configureEach {
                eachFile(sourceResourceFilter)
            }
            development?.let { configureDevelopmentClasspath(it) }
        }
    }

    fun include(owner: Project, sourceSet: SourceSet) {
        if (sources.none { it.sourceSet === sourceSet }) {
            sources.add(ResourceSource(owner, sourceSet))
        }
    }

    /** A single mod root for all loaders, without modifying compilation outputs. */
    fun developmentSourceSet(): SourceSet {
        development?.let { return it }
        val sourceSets = project.extensions.getByType<SourceSetContainer>()
        val directory = project.layout.buildDirectory.dir("mdk/developmentMod")
        val stage = project.tasks.register<Sync>("stageModForDevelopment") {
            description = "Stages the complete mod for development runs."
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            into(directory)
            from(resources)
            eachFile(ComposedResourceFilter(resourceDirectory.get().asFile))
        }
        // Resolve the contributors after all optional conventions have registered their outputs.
        project.afterEvaluate {
            stage.configure {
                for ((_, sourceSet) in sources) {
                    from(sourceSet.output)
                }
            }
        }
        return sourceSets.create("mdkDevelopment") {
            java.setSrcDirs(emptyList<String>())
            resources.setSrcDirs(emptyList<String>())
            (output.classesDirs as ConfigurableFileCollection).setFrom(stage)
            output.setResourcesDir(directory.get().asFile)
            output.dir(mapOf("builtBy" to stage), directory)
        }.also { development = it }
    }

    private fun configureDevelopmentClasspath(development: SourceSet) {
        val originalOutputs = project.files(sources.map { it.sourceSet.output })
        val commonJars = project.files(sources.map { it.owner }.distinct().filter { it != project }.map {
            it.tasks.named<Jar>("jar").flatMap { jar -> jar.archiveFile }
        })
        val sourceSets = project.extensions.getByType<SourceSetContainer>()
        // Keep each environment's dependencies (notably Fabric's client/server Minecraft jars),
        // replacing only the mod's own roots and shared project jars with the staged mod.
        sourceSets.matching { it.name !in setOf("config", "configClient", "mdkDevelopment") }.all {
            runtimeClasspath = development.output + (runtimeClasspath - originalOutputs - commonJars)
        }
        development.runtimeClasspath = sourceSets.named("main").get().runtimeClasspath
        development.compileClasspath = sourceSets.named("main").get().compileClasspath
    }
}

fun Project.resourceComposition(): ResourceComposition =
    extensions.findByType(ResourceComposition::class.java)
        ?: ResourceComposition(this).also { extensions.add("mdkResourceComposition", it) }

fun Project.developmentModSourceSet(): SourceSet = resourceComposition().developmentSourceSet()

internal fun Project.includeComposedResources(sourceSet: SourceSet) {
    val owner = rootProject.allprojects.first { candidate ->
        candidate.extensions.findByType(SourceSetContainer::class.java)?.any { it === sourceSet } == true
    }
    resourceComposition().include(owner, sourceSet)
}
