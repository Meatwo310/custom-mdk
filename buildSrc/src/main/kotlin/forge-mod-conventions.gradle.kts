import net.meatwo310.mdk.build.*
import net.minecraftforge.gradle.ForgeGradleExtension
import net.minecraftforge.gradle.MinecraftExtensionForProject
import org.gradle.api.file.DuplicatesStrategy

plugins {
    `java-library`
    idea
    id("net.minecraftforge.gradle")
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
val minecraftVersion: String by project
val minecraftVersionRange: String by project
val forgeVersion: String by project
val forgeVersionRange: String by project
val loaderVersionRange: String by project
val javaVersion: String by project
val forgeEventbusValidatorVersion = project.properties["forgeEventbusValidatorVersion"]?.toString()

val forgeFullVersion = "$minecraftVersion-$forgeVersion"
val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(commonProject)
evaluationDependsOn(sharedCommonProject)
configureCiRuntimeMods()

val minecraft = extensions.getByType(MinecraftExtensionForProject::class.java)
val fg = extensions.getByType(ForgeGradleExtension::class.java)

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:$forgeFullVersion").asProvider())
    annotationProcessor("${versionCatalog.module(VersionCatalogLibrary.Mixin)}:${versionCatalog.version(VersionCatalogVersion.Mixin)}:processor")
    if (forgeEventbusValidatorVersion != null) {
        annotationProcessor("net.minecraftforge:eventbus-validator:$forgeEventbusValidatorVersion")
    }
}

sourceSets.main {
    java.srcDirs(project(commonProject).sourceSets.main.get().java.srcDirs)
    java.srcDirs(project(sharedCommonProject).sourceSets.main.get().java.srcDirs)
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    exclude("**/*.bbmodel")
    exclude("src/generated/**/.cache")
}

base {
    archivesName = "$modId-$minecraftVersion-forge"
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(javaVersion.toInt())
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

configurations {
    val localRuntime by configurations.creating
    runtimeClasspath.get().extendsFrom(localRuntime)
}

minecraft.mappings("official", minecraftVersion)

minecraft.runs {
    configureEach {
        workingDir = layout.projectDirectory.dir("run")
        systemProperty("forge.enabledGameTestNamespaces", modId)
        mods {
            create(modId) {
                source(sourceSets.main.get())
            }
        }
    }

    create("client")

    create("server") {
        args("--nogui")
    }

    if (minecraftVersion.supportsGameTestServer()) {
        create("gameTestServer")
    }

    create("data") {
        workingDir = layout.projectDirectory.dir("run-data")
        args(
            "--mod", modId,
            "--all",
            "--output", layout.projectDirectory.dir("src/generated/resources"),
            "--existing", layout.projectDirectory.dir("src/main/resources"),
        )
    }
}

if (forgeEventbusValidatorVersion != null) {
    minecraft.runs.configureEach {
        systemProperty("eventbus.api.strictRuntimeChecks", "true")
    }
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "forge_version" to forgeVersion,
        "forge_version_range" to forgeVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription,
        "mod_credits" to modCredits,
        "mod_display_url" to modDisplayUrl,
        "mod_issue_tracker_url" to modIssueTrackerUrl,
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into(layout.buildDirectory.dir("generated/sources/modMetadata"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(generateModMetadata)
    manifest.attributes(mapOf("MixinConfigs" to "$modId.mixins.json"))
}
