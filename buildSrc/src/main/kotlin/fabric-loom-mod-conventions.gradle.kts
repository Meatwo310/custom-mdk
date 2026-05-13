import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("fabric-mod-conventions")
    id("net.fabricmc.fabric-loom")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val modId: String by project
val minecraftVersion: String by project
val fabricApiVersion: String by project

val commonProject = ":$minecraftVersion-common"
val sharedCommonProject = ":common"
evaluationDependsOn(sharedCommonProject)

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.named("client").get())
            sourceSet(project(sharedCommonProject).sourceSets.main.get())
            sourceSet(project(commonProject).sourceSets.main.get())
        }
    }

    runs.configureEach {
        ideConfigGenerated(true)
        if (name == "gameTest") {
            vmArg("-Dfabric.log.level=debug")
        }
    }
}

dependencies {
    minecraft("${libs.module("minecraft")}:$minecraftVersion")
    implementation(libs.library("fabric-loader"))
    implementation("${libs.module("fabric-api")}:$fabricApiVersion")
}

fabricApi {
    val testModId = "$modId-test"

    @Suppress("UnstableApiUsage")
    configureTests {
        createSourceSet = true
        modId = testModId
        enableGameTests = true
        enableClientGameTests = true
        eula = true
    }
}
