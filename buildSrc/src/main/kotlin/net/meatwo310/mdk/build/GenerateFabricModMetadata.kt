package net.meatwo310.mdk.build

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

abstract class GenerateFabricModMetadata : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val templateFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val defaultModMetadataJson: Property<String>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val mixinConfigFiles: ConfigurableFileCollection

    @TaskAction
    fun generate() {
        val defaults = parseObject(defaultModMetadataJson.get()).toMutableMap()
        val mixins = mixinConfigFiles.files
            .sortedWith(compareBy({ it.name.endsWith(".client.mixins.json") }, { it.name }))
            .distinctBy { it.name }
            .map { file ->
                if (file.name.endsWith(".client.mixins.json")) {
                    linkedMapOf("config" to file.name, "environment" to "client")
                } else {
                    file.name
                }
            }
        if (mixins.isNotEmpty()) defaults["mixins"] = mixins
        val overrides = parseObject(templateFile.get().asFile.readText())
        val modMetadata = mergeJsonObjects(defaults, overrides)
        outputFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(JsonOutput.prettyPrint(JsonOutput.toJson(modMetadata)) + "\n")
        }
    }

    private fun parseObject(json: String): Map<*, *> {
        val parsed = JsonSlurper().parseText(json)
        require(parsed is Map<*, *>) { "fabric.mod.json must contain a JSON object" }
        return parsed
    }

    private fun mergeJsonObjects(defaults: Map<*, *>, overrides: Map<*, *>): Map<String, Any?> {
        val merged = linkedMapOf<String, Any?>()
        for ((key, value) in defaults) {
            if (key is String) merged[key] = value
        }
        for ((key, overrideValue) in overrides) {
            if (key !is String) continue
            val defaultValue = merged[key]
            merged[key] = if (defaultValue is Map<*, *> && overrideValue is Map<*, *>) {
                mergeJsonObjects(defaultValue, overrideValue)
            } else {
                overrideValue
            }
        }
        return merged
    }
}
