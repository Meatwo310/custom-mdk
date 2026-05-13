import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `java-library`
    idea
    id("net.neoforged.moddev.legacyforge")
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
val minecraftVersion: String by project
val minecraftVersionRange: String by project
val forgeVersion: String by project
val forgeVersionRange: String by project
val loaderVersionRange: String by project
val parchmentMinecraftVersion: String by project
val parchmentMappingsVersion: String by project

val forgeFullVersion = "$minecraftVersion-$forgeVersion"
val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)

dependencies {
    implementation(project(commonProject))
    annotationProcessor("${libs.module("mixin")}:${libs.version("mixin")}:processor")
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
    languageVersion = JavaLanguageVersion.of(17)
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

configurations {
    val localRuntime by configurations.creating
    runtimeClasspath.get().extendsFrom(localRuntime)
}

legacyForge {
    version = forgeFullVersion
    validateAccessTransformers = true

    parchment {
        mappingsVersion = parchmentMappingsVersion
        minecraftVersion = parchmentMinecraftVersion
    }

    runs {
        create("client") {
            client()
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("data") {
            data()
            gameDirectory = project.file("run-data")
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(project(sharedCommonProject).sourceSets.main.get())
            sourceSet(project(commonProject).sourceSets.main.get())
        }
    }
}

mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
    config("$modId.mixins.json")
}

// When no @Mixin classes exist, the annotation processor generates no refmap/TSRG output.
// reobfJar passes the TSRG to ART via --names and fails with FileNotFoundException if missing.
// This task creates an empty-but-valid placeholder so the build succeeds regardless.
val ensureMixinRefmap = tasks.register("ensureMixinRefmap") {
    mustRunAfter(tasks.named("compileJava"))
    val tsrgPath = project.layout.buildDirectory.get().asFile.resolve("mixin/$modId.refmap.json.mappings.tsrg").absolutePath
    doLast {
        val tsrg = File(tsrgPath)
        if (!tsrg.exists()) {
            tsrg.parentFile.mkdirs()
            tsrg.writeText("tsrg2 left right\n")
        }
    }
}

tasks.named("reobfJar") {
    dependsOn(ensureMixinRefmap)
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

sourceSets.main.get().resources.srcDir(generateModMetadata)
legacyForge.ideSyncTask(generateModMetadata)

tasks.jar {
    from(project(sharedCommonProject).sourceSets.main.get().output)
    from(project(commonProject).sourceSets.main.get().output)
    manifest.attributes(mapOf("MixinConfigs" to "$modId.mixins.json"))
}
