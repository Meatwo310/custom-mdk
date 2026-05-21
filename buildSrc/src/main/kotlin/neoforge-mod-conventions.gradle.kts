import net.meatwo310.mdk.build.configureRuntimeMods
import net.meatwo310.mdk.build.supportsGameTestServer

plugins {
    `java-library`
    idea
    id("net.neoforged.moddev")
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
val neoVersion: String by project
val javaVersion: String by project

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)
val loaderVersionRange = project.properties["loaderVersionRange"]?.toString()
val parchmentMinecraftVersion = project.properties["parchmentMinecraftVersion"]?.toString()
val parchmentMappingsVersion = project.properties["parchmentMappingsVersion"]?.toString()
val neoDataRun = project.properties["neoDataRun"]?.toString() ?: "data"
configureRuntimeMods()

dependencies {
    implementation(project(commonProject))
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    exclude("**/*.bbmodel")
    exclude("src/generated/**/.cache")
}

base {
    archivesName = "$modId-$minecraftVersion-neo"
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

neoForge {
    version = neoVersion
    validateAccessTransformers = true

    if (parchmentMappingsVersion != null && parchmentMinecraftVersion != null) {
        parchment {
            mappingsVersion = parchmentMappingsVersion
            minecraftVersion = parchmentMinecraftVersion
        }
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        if (minecraftVersion.supportsGameTestServer()) {
            create("gameTestServer") {
                type = "gameTestServer"
                systemProperty("neoforge.enabledGameTestNamespaces", modId)
            }
        }

        create("data") {
            if (neoDataRun == "clientData") clientData() else data()
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
            logLevel = org.slf4j.event.Level.DEBUG
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

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = buildMap {
        put("minecraft_version", minecraftVersion)
        put("minecraft_version_range", minecraftVersionRange)
        put("neo_version", neoVersion)
        if (loaderVersionRange != null) put("loader_version_range", loaderVersionRange)
        put("mod_id", modId)
        put("mod_name", modName)
        put("mod_license", modLicense)
        put("mod_version", modVersion)
        put("mod_authors", modAuthors)
        put("mod_description", modDescription)
        put("mod_credits", modCredits)
        put("mod_display_url", modDisplayUrl)
        put("mod_issue_tracker_url", modIssueTrackerUrl)
    }
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into(layout.buildDirectory.dir("generated/sources/modMetadata"))
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

tasks.jar {
    from(project(sharedCommonProject).sourceSets.main.get().output)
    from(project(commonProject).sourceSets.main.get().output)
}
