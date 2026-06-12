package net.meatwo310.mdk.build

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.register

fun Project.configureCiRuntimeMods(): Configuration {
    val ciRuntimeMods = configurations.create("ciRuntimeMods") {
        isCanBeConsumed = false
        isCanBeResolved = true
        isTransitive = false
        description = "Additional runtime mod jars to stage for CI."
    }

    tasks.register<Sync>("collectCiRuntimeMods") {
        from(ciRuntimeMods)
        into(layout.buildDirectory.dir("ciRuntimeMods"))
    }

    tasks.named("build") {
        dependsOn("collectCiRuntimeMods")
    }

    return ciRuntimeMods
}
