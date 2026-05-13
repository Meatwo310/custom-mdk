import org.gradle.api.artifacts.VersionCatalogsExtension
import groovy.json.JsonOutput

plugins {
    `java-library`
    idea
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val modId: String by project
val modName: String by project
val modLicense: String by project
val modVersion: String by project
val modAuthors: String by project
val modDescription: String by project
val modDisplayUrl: String by project
val modIssueTrackerUrl: String by project
val modCredits: String by project
val modFabricEntrypoint: String by project
val modFabricClientEntrypoint: String by project
val minecraftVersion: String by project
val javaVersion: String by project

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)
val generatedModMetadataDir = layout.buildDirectory.dir("generated/sources/modMetadata")

dependencies {
    implementation(project(commonProject))
}

base {
    archivesName = "$modId-$minecraftVersion-fabric"
}

sourceSets.main.get().resources.srcDir(generatedModMetadataDir)

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to JsonOutput.toJson("~$minecraftVersion"),
        "loader_version" to JsonOutput.toJson(">=${libs.version("fabric-loader")}"),
        "mod_id" to JsonOutput.toJson(modId),
        "mod_name" to JsonOutput.toJson(modName),
        "mod_license" to JsonOutput.toJson(modLicense),
        "mod_version" to JsonOutput.toJson(modVersion),
        "mod_authors" to JsonOutput.toJson(
            modAuthors.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        ),
        "mod_description" to JsonOutput.toJson(modDescription),
        "mod_display_url" to JsonOutput.toJson(modDisplayUrl),
        "mod_issue_tracker_url" to JsonOutput.toJson(modIssueTrackerUrl),
        "mod_credits" to JsonOutput.toJson(modCredits),
        "mod_entrypoint" to JsonOutput.toJson(modFabricEntrypoint),
        "mod_client_entrypoint" to JsonOutput.toJson(modFabricClientEntrypoint),
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into(generatedModMetadataDir)
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
