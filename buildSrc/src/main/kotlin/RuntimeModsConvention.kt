import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.register

fun Project.configureRuntimeMods(): Configuration {
    val runtimeMods = configurations.create("runtimeMods") {
        isCanBeConsumed = false
        isCanBeResolved = true
        isTransitive = false
        description = "Additional mod jars to stage into run/mods for external runtime tests."
    }

    tasks.register<Sync>("collectRuntimeMods") {
        from(runtimeMods)
        into(layout.buildDirectory.dir("runtimeMods"))
    }

    tasks.named("build") {
        dependsOn("collectRuntimeMods")
    }

    return runtimeMods
}
