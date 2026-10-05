package net.meatwo310.mdk.build

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

fun Project.includeSourceSetArtifacts(vararg sourceSets: SourceSet) {
    val mainSources = extensions.getByType<SourceSetContainer>()
        .named(SourceSet.MAIN_SOURCE_SET_NAME).get().allSource
    tasks.named<Jar>("jar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        for (sourceSet in sourceSets) {
            from(sourceSet.output)
        }
    }
    // Common projects do not necessarily create a sources jar. Also support
    // conventions that enable sources jars after including a source set.
    tasks.withType<Jar>().matching { it.name == "sourcesJar" }.configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        // withSourcesJar may add its default sources after this action runs.
        // Always give the project's own sources precedence over shared sources.
        from(mainSources)
        for (sourceSet in sourceSets) {
            from(sourceSet.allSource)
        }
    }
}
