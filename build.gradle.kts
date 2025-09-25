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

// Kotlin DSL-friendly property accessor
fun Project.prop(name: String): String = findProperty(name) as? String ?: error("Property '$name' not found")

// Extension properties for cleaner access to gradle.properties
val Project.modVersion get() = prop("mod_version")
val Project.modGroupId get() = prop("mod_group_id") 
val Project.modId get() = prop("mod_id")
val Project.minecraftVersion get() = prop("minecraft_version")
val Project.minecraftVersionRange get() = prop("minecraft_version_range")
val Project.mappingChannel get() = prop("mapping_channel")
val Project.mappingVersion get() = prop("mapping_version")
val Project.forgeVersion get() = prop("forge_version")
val Project.forgeVersionRange get() = prop("forge_version_range")
val Project.loaderVersionRange get() = prop("loader_version_range")
val Project.modName get() = prop("mod_name")
val Project.modLicense get() = prop("mod_license")
val Project.modAuthors get() = prop("mod_authors")
val Project.modDescription get() = prop("mod_description")

version = "v$modVersion"
group = modGroupId

base {
    archivesName.set("$modId-forge-$minecraftVersion")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println("Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), Arch: ${System.getProperty("os.arch")}")

minecraft {
    mappings(mappingChannel, mappingVersion)
    copyIdeResources.set(true)
//    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.configureEach {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "REGISTRIES")
        property("forge.logging.console.level", "debug")

        mods.create(modId) {
            source(sourceSets.main.get())
        }

        property("mixin.env.remapRefMap", "true")
        property("mixin.env.refMapRemappingFile", "${project.projectDir}/build/createSrgToMcp/output.srg")
    }

    runs {
        create("client") {
            property("forge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", modId)
            args("--nogui")
        }

        create("gameTestServer") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", modId)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", modId, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))
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
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
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
    add(sourceSets.main.get(), "$modId.refmap.json")
    config("$modId.mixins.json")
}

tasks.named<ProcessResources>("processResources") {
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
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + ("project" to project))
    }
}

tasks.named<Jar>("jar") {
    manifest.attributes(
        "Specification-Title" to modId,
        "Specification-Vendor" to modAuthors,
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to archiveVersion,
        "Implementation-Vendor" to modAuthors,
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
