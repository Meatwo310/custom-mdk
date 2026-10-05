package net.meatwo310.mdk.build

import java.io.File
import java.util.zip.ZipFile
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

class ResourceCompositionTest {
    @get:Rule
    val temporary = TemporaryFolder()

    private val targets = listOf("neo", "legacy", "forge", "fabric", "disabled", "optional", "late")
    // Highest priority first: project precedence, then source-set precedence.
    private val inputs = listOf(
        Input("platform", "configClient"), Input("platform", "client"),
        Input("platform", "config"), Input("platform", "main"),
        Input("version", "config"), Input("version", "main"),
        Input("common", "config"), Input("common", "main"),
    )

    @Test
    fun `resources and classes remain consistent across archives and development builds`() {
        val root = temporary.newFolder("resource-regression")
        val cases = resourceCases()
        prepareBuild(root, cases)
        val tasks = targets.flatMap { target ->
            listOf("jar", "sourcesJar", "stageModForDevelopment", "verifyRuntimeRoots").map { ":$target:$it" }
        }

        run(root, tasks)
        verify(root, cases)

        val reused = run(root, tasks)
        assertContains(reused.output, "Reusing configuration cache.")
        tasks.forEach { task ->
            // Verification tasks have no outputs and must execute on every build.
            val expectedOutcome = if (task.endsWith(":verifyRuntimeRoots")) TaskOutcome.SUCCESS else TaskOutcome.UP_TO_DATE
            assertEquals(expectedOutcome, reused.task(task)?.outcome, task)
        }
        verify(root, cases)

        targets.forEach { target ->
            root.resolve("$target/src/main/resources/deleted.txt").delete()
            inputs.filter { it.owner == "platform" }.forEach { input ->
                root.resolve("$target/src/${input.sourceSet}/resources/all.txt").delete()
            }
        }
        cases.remove("deleted.txt")
        cases["all.txt"] = inputs.filter { it.owner != "platform" }

        // Stage first, then archive: development runs must not contaminate later jars.
        run(root, targets.map { ":$it:stageModForDevelopment" } + tasks)
        verify(root, cases)
        listOf("common", "version").forEach { owner ->
            assertTrue(root.resolve("$owner/build/libs").walkTopDown().none { it.name.endsWith("-sources.jar") }, owner)
        }
    }

    private fun resourceCases(): MutableMap<String, List<Input>> {
        val cases = linkedMapOf<String, List<Input>>()
        var pair = 0
        inputs.forEachIndexed { index, first ->
            inputs.drop(index + 1).forEach { second -> cases["pair-${pair++}.txt"] = listOf(first, second) }
        }
        cases["all.txt"] = inputs
        inputs.forEach { cases["unique-${it.owner}-${it.sourceSet}.txt"] = listOf(it) }
        listOf("deleted.txt", "expanded.txt", "generated.txt").forEach {
            cases[it] = listOf(Input("platform", "main"))
        }
        cases["assets/examplemod/probe.txt"] = inputs
        cases["data/examplemod/probe.txt"] = inputs
        return cases
    }

    private fun prepareBuild(root: File, cases: Map<String, List<Input>>) {
        write(root.resolve("settings.gradle.kts"), """
            rootProject.name = "resource-regression"
            include("common", "version", ${targets.joinToString { "\"$it\"" }})
        """.trimIndent() + "\n")
        copyResource("build.gradle.kts", root.resolve("build.gradle.kts"))
        write(root.resolve("buildSrc/build.gradle.kts"), "plugins { `kotlin-dsl` }\nrepositories { mavenCentral() }\n")
        listOf("SourceSetArtifacts.kt", "ConfigSourceSets.kt", "ResourceComposition.kt").forEach { name ->
            copyResource("helpers/$name", root.resolve("buildSrc/src/main/kotlin/net/meatwo310/mdk/build/$name"))
        }
        cases.forEach { (name, candidates) ->
            candidates.forEach { input ->
                projects(input).forEach { project ->
                    write(root.resolve("$project/src/${input.sourceSet}/resources/$name"), input.content)
                }
            }
        }
        targets.forEach { target ->
            root.resolve("$target/src/main/resources/generated.txt").delete()
            write(root.resolve("$target/src/main/resources/expanded.txt"), "\${probe}\n")
            write(root.resolve("$target/templates/generated.txt"), "\${probe}\n")
        }
        inputs.forEach { input ->
            projects(input).forEach { project ->
                write(
                    root.resolve("$project/src/${input.sourceSet}/java/${input.className}.java"),
                    "package fixture; public class ${input.owner}_${input.sourceSet} {}\n",
                )
            }
        }
    }

    private fun run(root: File, tasks: List<String>) = GradleRunner.create()
        .withProjectDir(root)
        .withArguments(tasks + listOf(
            "--no-parallel", "--max-workers=1", "--configuration-cache", "--configuration-cache-problems=fail",
            "--stacktrace", "-Dorg.gradle.jvmargs=-Xmx512m", "-Pkotlin.compiler.execution.strategy=in-process",
        ))
        .build()

    private fun verify(root: File, cases: Map<String, List<Input>>) {
        targets.forEach { target ->
            val expected = cases.mapNotNull { (name, candidates) ->
                candidates.firstOrNull { enabled(target, it) }?.let { name to it.content }
            }.toMap()
            val expectedClasses = inputs.filter { enabled(target, it) }.map { it.className }.toSet()
            val stage = root.resolve("$target/build/mdk/developmentMod")
            val merged = root.resolve("$target/build/mdk/composedResources")
            ZipFile(root.resolve("$target/build/libs/$target.jar")).use { runtime ->
                ZipFile(root.resolve("$target/build/libs/$target-sources.jar")).use { sources ->
                    listOf(runtime, sources).forEach { archive ->
                        val entries = archive.entries().asSequence().toList()
                        assertEquals(entries.size, entries.map { it.name }.toSet().size, "$target: duplicate archive entries")
                        val actual = entries.filter { it.name.endsWith(".txt") }.associate { entry ->
                            entry.name to archive.getInputStream(entry).bufferedReader(Charsets.UTF_8).use { it.readText() }
                        }
                        assertEquals(expected, actual, "$target: ${archive.name}")
                    }
                    assertEquals(expectedClasses, archiveClasses(runtime, ".class"), "$target: runtime classes")
                    assertEquals(expectedClasses, archiveClasses(sources, ".java"), "$target: source classes")
                }
            }
            listOf(stage, merged).forEach { directory ->
                val actual = directory.walkTopDown().filter { it.isFile && it.extension == "txt" }.associate {
                    it.relativeTo(directory).invariantSeparatorsPath to it.readText()
                }
                assertEquals(expected, actual, "$target: $directory")
            }
            val stagedClasses = stage.walkTopDown().filter { it.isFile && it.extension == "class" }.map {
                it.relativeTo(stage).invariantSeparatorsPath.removeSuffix(".class")
            }.toSet()
            assertEquals(expectedClasses, stagedClasses, "$target: staged classes")
        }
    }

    private fun archiveClasses(archive: ZipFile, suffix: String) = archive.entries().asSequence()
        .map { it.name }.filter { it.endsWith(suffix) }.map { it.removeSuffix(suffix) }.toSet()

    private fun enabled(target: String, input: Input): Boolean = with(input) {
        if (owner != "platform") {
            sourceSet == "main" || (target != "disabled" && !(target == "optional" && owner == "version"))
        } else {
            sourceSet == "main" || (sourceSet == "config" && target != "disabled") ||
                (sourceSet == "configClient" && target in listOf("neo", "fabric")) ||
                (sourceSet == "client" && target == "fabric")
        }
    }

    private fun projects(input: Input) = if (input.owner == "platform") targets else listOf(input.owner)

    private fun copyResource(name: String, destination: File) {
        val content = requireNotNull(javaClass.getResourceAsStream("/resource-composition/$name")) {
            "Missing fixture resource: $name"
        }.bufferedReader(Charsets.UTF_8).use { it.readText() }
        write(destination, content)
    }

    private fun write(file: File, content: String) {
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    private data class Input(val owner: String, val sourceSet: String) {
        val content get() = "$owner:$sourceSet\n"
        val className get() = "fixture/${owner}_$sourceSet"
    }
}
