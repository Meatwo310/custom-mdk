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
    const val MINECRAFT_VERSION = "1.20.1"
    const val MINECRAFT_VERSION_RANGE = "[1.20.1,1.21)"
    const val FORGE_VERSION = "47.4.0"
    const val FORGE_VERSION_RANGE = "[47.4,)"
    const val LOADER_VERSION_RANGE = "[47,)"
    const val MAPPING_CHANNEL = "parchment"
    const val MAPPING_VERSION = "2023.09.03-1.20.1"

    // Mod Properties
    const val MOD_ID = "examplemod"
    const val MOD_NAME = "Example Mod"
    const val MOD_LICENSE = "MIT"
    const val MOD_VERSION = "0.1.0"
    const val MOD_GROUP_ID = "net.meatwo310.examplemod"
    const val MOD_AUTHORS = "Meatwo310"
    const val MOD_DESCRIPTION = ""
}

version = "v${ModConfig.MOD_VERSION}"
group = ModConfig.MOD_GROUP_ID

base {
    archivesName.set("${ModConfig.MOD_ID}-${ModConfig.MINECRAFT_VERSION}-forge")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        @Suppress("UnstableApiUsage")
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

with(System.getProperties()) {
    println("Java: ${get("java.version")}, JVM: ${get("java.vm.version")} (${get("java.vendor")}), Arch: ${get("os.arch")}")
}

tasks.withType<JavaCompile>().configureEach {
    doFirst {
        with(javaCompiler.get().metadata) {
            println("Compiling with Java: $javaRuntimeVersion, JVM: $jvmVersion ($vendor)")
        }
    }
}

minecraft {
    mappings(ModConfig.MAPPING_CHANNEL, ModConfig.MAPPING_VERSION)
    copyIdeResources.set(true)
//    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.configureEach {
        workingDirectory(project.file("run"))
        property("forge.logging.markers", "REGISTRIES")
        property("forge.logging.console.level", "debug")

        mods.create(ModConfig.MOD_ID) {
            source(sourceSets.main.get())
        }

        property("mixin.env.remapRefMap", "true")
        property("mixin.env.refMapRemappingFile", "${project.projectDir}/build/createSrgToMcp/output.srg")
    }

    runs {
        create("client") {
            property("forge.enabledGameTestNamespaces", ModConfig.MOD_ID)
        }

        create("server") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.MOD_ID)
            args("--nogui")
        }

        create("gameTestServer") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", ModConfig.MOD_ID)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", ModConfig.MOD_ID, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))
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
    minecraft("net.minecraftforge:forge:${ModConfig.MINECRAFT_VERSION}-${ModConfig.FORGE_VERSION}")
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
    add(sourceSets.main.get(), "${ModConfig.MOD_ID}.refmap.json")
    config("${ModConfig.MOD_ID}.mixins.json")
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "minecraft_version" to ModConfig.MINECRAFT_VERSION,
        "minecraft_version_range" to ModConfig.MINECRAFT_VERSION_RANGE,
        "forge_version" to ModConfig.FORGE_VERSION,
        "forge_version_range" to ModConfig.FORGE_VERSION_RANGE,
        "loader_version_range" to ModConfig.LOADER_VERSION_RANGE,
        "mod_id" to ModConfig.MOD_ID,
        "mod_name" to ModConfig.MOD_NAME,
        "mod_license" to ModConfig.MOD_LICENSE,
        "mod_version" to ModConfig.MOD_VERSION,
        "mod_authors" to ModConfig.MOD_AUTHORS,
        "mod_description" to ModConfig.MOD_DESCRIPTION,
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + ("project" to project))
    }
}

tasks.named<Jar>("jar") {
    manifest.attributes(
        "Specification-Title" to ModConfig.MOD_ID,
        "Specification-Vendor" to ModConfig.MOD_AUTHORS,
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to archiveVersion,
        "Implementation-Vendor" to ModConfig.MOD_AUTHORS,
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
