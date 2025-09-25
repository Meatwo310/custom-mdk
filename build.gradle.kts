@file:Suppress("PropertyName")

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    eclipse
    idea
    `maven-publish`
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("org.spongepowered.mixin") version "0.7.+"
}

object ModConfig {
    // Environment Properties
    const val minecraftVersion = "1.20.1"
    const val minecraftVersionRange = "[1.20.1,1.21)"
    const val forgeVersion = "47.4.0"
    const val forgeVersionRange = "[47.4,)"
    const val loaderVersionRange = "[47,)"
    const val mappingChannel = "parchment"
    const val mappingVersion = "2023.09.03-1.20.1"
    
    // Mod Properties
    const val modId = "examplemod"
    const val modName = "Example Mod"
    const val modLicense = "MIT"
    const val modVersion = "0.1.0"
    const val modGroupId = "io.github.meatwo310.examplemod"
    const val modAuthors = "Meatwo310"
    const val modDescription = ""
}

version = "v${ModConfig.modVersion}"
group = ModConfig.modGroupId

base {
    archivesName.set("${ModConfig.modId}-forge-${ModConfig.minecraftVersion}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println("Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), Arch: ${System.getProperty("os.arch")}")

minecraft {
    mappings(ModConfig.mappingChannel, ModConfig.mappingVersion)
    copyIdeResources.set(true)
//    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.configureEach {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "REGISTRIES")
        property("forge.logging.console.level", "debug")

        mods.create(ModConfig.modId) {
            source(sourceSets.main.get())
        }

        property("mixin.env.remapRefMap", "true")
        property("mixin.env.refMapRemappingFile", "${project.projectDir}/build/createSrgToMcp/output.srg")
    }

    runs {
        create("client") {
            property("forge.enabledGameTestNamespaces", ModConfig.modId)
        }

        create("server") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.modId)
            args("--nogui")
        }

        create("gameTestServer") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.modId)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", ModConfig.modId, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

repositories {
//    flatDir {
//        dir("libs")
//    }

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

    repositories {
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://cursemaven.com")
                }
            }
            forRepositories(fg.repository)
            filter {
                includeGroup("curse.maven")
            }
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:${ModConfig.minecraftVersion}-${ModConfig.forgeVersion}")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

//    // Mixin Extras
//    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")!!)
//    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0")) {
//        jarJar.ranged(this, "[0.5.0,)")
//    }

    // Default Dependencies
    runtimeOnly(fg.deobf("curse.maven:catalogue-459701:4766090"))
    runtimeOnly(fg.deobf("curse.maven:configured-457570:5180900"))
    runtimeOnly(fg.deobf("curse.maven:jade-324717:6855440"))
    runtimeOnly(fg.deobf("curse.maven:jei-238222:6600311"))
    runtimeOnly(fg.deobf("curse.maven:jei-integration-265917:4999754"))
}

mixin {
    add(sourceSets.main.get(), "${ModConfig.modId}.refmap.json")
    config("${ModConfig.modId}.mixins.json")
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "minecraft_version" to ModConfig.minecraftVersion,
        "minecraft_version_range" to ModConfig.minecraftVersionRange,
        "forge_version" to ModConfig.forgeVersion,
        "forge_version_range" to ModConfig.forgeVersionRange,
        "loader_version_range" to ModConfig.loaderVersionRange,
        "mod_id" to ModConfig.modId,
        "mod_name" to ModConfig.modName,
        "mod_license" to ModConfig.modLicense,
        "mod_version" to ModConfig.modVersion,
        "mod_authors" to ModConfig.modAuthors,
        "mod_description" to ModConfig.modDescription,
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + ("project" to project))
    }
}

tasks.named<Jar>("jar") {
    manifest.attributes(
        "Specification-Title" to ModConfig.modId,
        "Specification-Vendor" to ModConfig.modAuthors,
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to archiveVersion,
        "Implementation-Vendor" to ModConfig.modAuthors,
        "Implementation-Timestamp" to ZonedDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
    )
    finalizedBy("reobfJar")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

tasks.register("setupServer") {
    group = "custom"
    description = "Sets up the server run directory with eula.txt and server.properties"

    doLast {
        project.run {
            file("run-server").mkdirs()
            file("run-server/eula.txt").writeText("eula=true")
            file("run-server/server.properties").writeText("""
                allow-flight=true
                enable-command-block=true
                gamemode=creative
                online-mode=false
                """.trimIndent()
            )
        }
    }
}
