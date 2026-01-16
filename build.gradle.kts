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
    const val minecraft_version = "1.20.1"
    const val minecraft_version_range = "[1.20.1,1.21)"
    const val forge_version = "47.4.0"
    const val forge_version_range = "[47.4,)"
    const val loader_version_range = "[47,)"
    const val mapping_channel = "parchment"
    const val mapping_version = "2023.09.03-1.20.1"

    const val mod_id = "examplemod"
    const val mod_name = "Example Mod"
    const val mod_license = "MIT"
    const val mod_version = "0.1.0"
    const val mod_group_id = "io.github.meatwo310.examplemod"
    const val mod_authors = "Meatwo310"
    const val mod_description = ""
}

version = ModConfig.mod_version
group = ModConfig.mod_group_id

base {
    archivesName.set("${ModConfig.mod_id}-forge-${ModConfig.minecraft_version}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println("Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), Arch: ${System.getProperty("os.arch")}")

minecraft {
    mappings(ModConfig.mapping_channel, ModConfig.mapping_version)
    copyIdeResources.set(true)
//    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.configureEach {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "REGISTRIES")
        property("forge.logging.console.level", "debug")

        mods.create(ModConfig.mod_id) {
            source(sourceSets.main.get())
        }

        property("mixin.env.remapRefMap", "true")
        property("mixin.env.refMapRemappingFile", "${project.projectDir}/build/createSrgToMcp/output.srg")
    }

    runs {
        create("client") {
            property("forge.enabledGameTestNamespaces", ModConfig.mod_id)
        }

        create("server") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.mod_id)
            args("--nogui")
        }

        create("gameTestServer") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.mod_id)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", ModConfig.mod_id, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))
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
        forRepositories(fg.repository)
        filter {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:${ModConfig.minecraft_version}-${ModConfig.forge_version}")
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

    // Mod Dependencies
}

mixin {
    add(sourceSets.main.get(), "${ModConfig.mod_id}.refmap.json")
    config("${ModConfig.mod_id}.mixins.json")
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "minecraft_version" to ModConfig.minecraft_version,
        "minecraft_version_range" to ModConfig.minecraft_version_range,
        "forge_version" to ModConfig.forge_version,
        "forge_version_range" to ModConfig.forge_version_range,
        "loader_version_range" to ModConfig.loader_version_range,
        "mod_id" to ModConfig.mod_id,
        "mod_name" to ModConfig.mod_name,
        "mod_license" to ModConfig.mod_license,
        "mod_version" to ModConfig.mod_version,
        "mod_authors" to ModConfig.mod_authors,
        "mod_description" to ModConfig.mod_description,
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + ("project" to project))
    }
}

tasks.named<Jar>("jar") {
    manifest.attributes(
        "Specification-Title" to ModConfig.mod_id,
        "Specification-Vendor" to ModConfig.mod_authors,
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to archiveVersion,
        "Implementation-Vendor" to ModConfig.mod_authors,
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
