package net.meatwo310.mdk.build

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class GenerateLexForgeModMetadata : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val templateDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Input
    abstract val properties: MapProperty<String, String>

    @get:Input
    abstract val additionalModsToml: Property<String>

    init {
        additionalModsToml.convention("")
    }

    @TaskAction
    fun generate() {
        val sourceRoot = templateDirectory.get().asFile
        val targetRoot = outputDirectory.get().asFile
        targetRoot.deleteRecursively()

        sourceRoot.walkTopDown()
            .filter { it.isFile }
            .forEach { source ->
                val target = targetRoot.resolve(source.relativeTo(sourceRoot))
                target.parentFile.mkdirs()

                val expanded = properties.get().entries.fold(source.readText()) { text, (key, value) ->
                    text.replace("${'$'}{$key}", value)
                }
                val suffix = if (source.invariantSeparatorsPath.endsWith("/META-INF/mods.toml")) {
                    additionalModsToml.get()
                } else {
                    ""
                }
                target.writeText(expanded + suffix)
            }
    }
}
