plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.120"
    idea
}

val parchment_minecraft_version: String by project
val parchment_mappings_version: String by project
val mod_version: String by project
val mod_group_id: String by project
val mod_id: String by project
val minecraft_version: String by project
val minecraft_version_range: String by project
val neo_version: String by project
val loader_version_range: String by project
val mod_name: String by project
val mod_license: String by project
val mod_authors: String by project
val mod_description: String by project

tasks.wrapper.configure {
    distributionType = Wrapper.DistributionType.BIN
}

version = mod_version
group = mod_group_id

base {
    archivesName.set("$mod_id-$minecraft_version")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

neoForge {
    version = neo_version

    parchment {
        mappingsVersion = parchment_mappings_version
        minecraftVersion = parchment_minecraft_version
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            gameDirectory = file("run-server")
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            gameDirectory = file("run-server")
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            data()
            gameDirectory = file("run-data")
            programArguments.addAll(
                "--mod",
                mod_id,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

val localRuntime: Configuration by configurations.creating

configurations {
    runtimeClasspath {
        extendsFrom(localRuntime)
    }
}

repositories {
//    exclusiveContent {
//        forRepository {
//            maven {
//                name = "Modrinth"
//                url = uri("https://api.modrinth.com/maven")
//            }
//        }
//        forRepositories(fg.repository)
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
    localRuntime("curse.maven:catalogue-459701:6926815")
    localRuntime("curse.maven:configured-457570:7122915")
    localRuntime("curse.maven:jade-324717:6853386")
    localRuntime("curse.maven:jei-238222:7057366")
    localRuntime("curse.maven:jei-integration-reborn-1291136:7078944")
    localRuntime("curse.maven:kotlin-for-forge-351264:6994056")

    // Mod Dependencies
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replaceProperties = mapOf(
        "minecraft_version"       to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version"             to neo_version,
        "loader_version_range"    to loader_version_range,
        "mod_id"                  to mod_id,
        "mod_name"                to mod_name,
        "mod_license"             to mod_license,
        "mod_version"             to mod_version,
        "mod_authors"             to mod_authors,
        "mod_description"         to mod_description
    )
    inputs.properties(replaceProperties)

    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}
sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

tasks.compileJava {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
