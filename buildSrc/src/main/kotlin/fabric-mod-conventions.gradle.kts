import groovy.json.JsonOutput
import net.meatwo310.mdk.build.*

plugins {
    `java-library`
    idea
}

val modId: String by project
val modName: String by project
val modLicense: String by project
val modVersion: String by project
val modAuthors: String by project
val modDescription: String by project
val modDisplayUrl: String by project
val modIssueTrackerUrl: String by project
val modCredits: String by project
val modFabricEnvironment: String by project
val modFabricEntrypoint: String by project
val modFabricClientEntrypoint: String by project
val minecraftVersion: String by project
val javaVersion: String by project

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)
val generatedModMetadataDir = layout.buildDirectory.dir("generated/sources/modMetadata")
val fabricModMetadata = extensions.create<FabricModMetadataExtension>("fabricModMetadata")
configureRuntimeMods()

dependencies {
    implementation(project(commonProject))
}

base {
    archivesName = "$modId-$minecraftVersion-fabric"
}

sourceSets.main.get().resources.srcDir(generatedModMetadataDir)

fabricModMetadata.depends.putAll(linkedMapOf(
    "fabricloader" to ">=${versionCatalog.version(VersionCatalogVersion.FabricLoader)}",
    "minecraft" to "~$minecraftVersion",
    "java" to ">=$javaVersion",
))

fun defaultModMetadata(fabricDependencies: Map<String, String>) = linkedMapOf<String, Any?>(
    "schemaVersion" to 1,
    "id" to modId,
    "version" to modVersion,
    "name" to modName,
    "description" to modDescription,
    "authors" to modAuthors.split(",").map { it.trim() }.filter { it.isNotEmpty() },
    "contact" to linkedMapOf(
        "homepage" to modDisplayUrl,
        "issues" to modIssueTrackerUrl,
        "sources" to modDisplayUrl,
    ),
    "license" to modLicense,
    "custom" to linkedMapOf(
        "credits" to modCredits,
    ),
    "environment" to modFabricEnvironment,
    "entrypoints" to linkedMapOf(
        "main" to listOf(modFabricEntrypoint),
        "client" to listOf(modFabricClientEntrypoint),
    ),
    "depends" to fabricDependencies,
)

val generateModMetadata = tasks.register<GenerateFabricModMetadata>("generateModMetadata") {
    templateFile.set(layout.projectDirectory.file("src/main/templates/fabric.mod.json"))
    outputFile.set(generatedModMetadataDir.map { it.file("fabric.mod.json") })
    defaultModMetadataJson.set(fabricModMetadata.depends.map { JsonOutput.toJson(defaultModMetadata(it)) })
}

tasks.processResources {
    dependsOn(generateModMetadata)
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(javaVersion.toInt())
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

tasks.withType<JavaCompile>().configureEach {
    options.release = javaVersion.toInt()
}

java {
    withSourcesJar()
}

tasks.named("sourcesJar") {
    dependsOn(generateModMetadata)
}

tasks.jar {
    from(project(sharedCommonProject).sourceSets.main.get().output)
    from(project(commonProject).sourceSets.main.get().output)
}
