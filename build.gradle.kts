plugins {
    `java-library`
    `maven-publish`
    idea
    id("net.neoforged.moddev") version "2.0.141"
}

/// Mod Settings ///

object ModConfig {
    // Environment Properties
    const val MINECRAFT_VERSION = "26.1"
    const val MINECRAFT_VERSION_RANGE = "[26.1]"
    const val NEO_VERSION = "26.1.0.19-beta"

    // Mod Properties
    const val MOD_ID = "examplemod"
    const val MOD_NAME = "Example Mod"
    const val MOD_LICENSE = "MIT"
    const val MOD_VERSION = "0.1.0"
    const val MOD_GROUP_ID = "net.meatwo310.examplemod"
    const val MOD_AUTHORS = "Meatwo310"
    const val MOD_DESCRIPTION = ""
    const val MOD_DISPLAY_URL = "https://github.com/Meatwo310/custom-mdk"
    const val MOD_ISSUE_TRACKER_URL = "${MOD_DISPLAY_URL}/issues"
    const val MOD_CREDITS = ""
}

repositories {
//    flatDir {
//        dir("libs")
//    }

//    maven {
//        name = "ModMaven"
//        url = uri("https://modmaven.dev/")
//    }

//    exclusiveContent {
//        forRepository {
//            maven {
//                name = "Modrinth"
//                url = uri("https://api.modrinth.com/maven")
//            }
//        }
//        filter {
//            includeGroup("maven.modrinth")
//        }
//    }

    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://cursemaven.com")
            }
        }
        filter {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    // Default Dependencies

    // Mod Dependencies
}

/// Project Settings ///

version = "v${ModConfig.MOD_VERSION}"
group = ModConfig.MOD_GROUP_ID

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")

    exclude("**/*.bbmodel")
    exclude("src/generated/**/.cache")
}

base {
    archivesName = "${ModConfig.MOD_ID}-${ModConfig.MINECRAFT_VERSION}-neo"
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(25)
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

with(System.getProperties()) {
    val version = get("java.version")
    val vmVersion = get("java.vm.version")
    val vendor = get("java.vendor")
    val arch = get("os.arch")
    println("Configuring with Java: $version, JVM: $vmVersion ($vendor), Arch: $arch")
}

tasks.withType<JavaCompile>().configureEach {
    doFirst {
        with(javaCompiler.get().metadata) {
            println("Compiling with Java: $javaRuntimeVersion, JVM: $jvmVersion ($vendor)")
        }
    }
}

neoForge {
    version = ModConfig.NEO_VERSION

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", ModConfig.MOD_ID)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", ModConfig.MOD_ID)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", ModConfig.MOD_ID)
        }

        create("data") {
            clientData()
            // gameDirectory = project.file("run-data")
            programArguments.addAll(
                "--mod", ModConfig.MOD_ID,
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
        create(ModConfig.MOD_ID) {
            sourceSet(sourceSets.main.get())
        }
    }
}

configurations {
    val localRuntime by configurations.creating
    runtimeClasspath.get().extendsFrom(localRuntime)
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to ModConfig.MINECRAFT_VERSION,
        "minecraft_version_range" to ModConfig.MINECRAFT_VERSION_RANGE,
        "neo_version" to ModConfig.NEO_VERSION,
        "mod_id" to ModConfig.MOD_ID,
        "mod_name" to ModConfig.MOD_NAME,
        "mod_license" to ModConfig.MOD_LICENSE,
        "mod_version" to ModConfig.MOD_VERSION,
        "mod_authors" to ModConfig.MOD_AUTHORS,
        "mod_description" to ModConfig.MOD_DESCRIPTION,
        "mod_credits" to ModConfig.MOD_CREDITS,
        "mod_display_url" to ModConfig.MOD_DISPLAY_URL,
        "mod_issue_tracker_url" to ModConfig.MOD_ISSUE_TRACKER_URL,
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
