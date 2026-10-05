"""Run resource composition regression tests without configuring Minecraft projects.

Usage: JAVA_HOME=/path/to/jdk python3 buildSrc/src/test/resource-composition/verify.py
Requires the repository's Gradle wrapper and a JDK supported by that wrapper.
"""

from itertools import combinations
from pathlib import Path
import os
import shutil
import subprocess
import tempfile
from zipfile import ZipFile

REPO = Path(__file__).resolve().parents[4]
TARGETS = ("neo", "legacy", "forge", "fabric", "disabled", "optional", "late")
INPUTS = (
    ("platform", "configClient"), ("platform", "client"),
    ("platform", "config"), ("platform", "main"),
    ("version", "config"), ("version", "main"),
    ("common", "config"), ("common", "main"),
)
BUILD = '''
import net.meatwo310.mdk.build.*

subprojects { apply(plugin = "java-library") }
val shared = project(":common")
val versionProject = project(":version")
val sharedConfig = shared.configureConfigSourceSet()
val versionConfig = versionProject.configureConfigSourceSet()
shared.includeConfigOutput(sharedConfig)
versionProject.includeConfigOutput(versionConfig)
subprojects.filter { it.name !in setOf("common", "version") }.forEach { target ->
    with(target) {
        val sets = extensions.getByType<SourceSetContainer>()
        val java = extensions.getByType<JavaPluginExtension>()
        if (name != "late") java.withSourcesJar()
        includeSourceSetArtifacts(
            versionProject.extensions.getByType<SourceSetContainer>().getByName("main"),
            shared.extensions.getByType<SourceSetContainer>().getByName("main"),
        )
        if (name == "fabric") {
            val client = sets.create("client")
            tasks.named<Jar>("jar") { from(client.output) }
            tasks.named<Jar>("sourcesJar") { from(client.allSource) }
        }
        if (name != "disabled") {
            val config = configureConfigSourceSet()
            val sharedSets = SharedConfigSourceSets(sharedConfig, if (name == "optional") null else versionConfig)
            val configSets = if (name in setOf("neo", "fabric")) {
                arrayOf(config, configureConfigSourceSet(CONFIG_CLIENT_SOURCE_SET_NAME))
            } else arrayOf(config)
            includeConfigOutput(sharedSets, *configSets)
            if (name == "fabric") {
                addConfigOutputTo("main", sharedSets, config)
                addConfigOutputTo("client", sharedSets, *configSets)
            } else if (name != "forge") {
                addConfigOutputTo("main", sharedSets, *configSets)
            }
        }
        if (name == "late") java.withSourcesJar()
        val generated = tasks.register<Copy>("generateProbe") {
            from(layout.projectDirectory.dir("templates"))
            into(layout.buildDirectory.dir("generatedResources"))
            expand(mapOf("probe" to "platform:main"))
        }
        sets.named("main") { resources.srcDir(generated) }
        tasks.named<ProcessResources>("processResources") {
            filesMatching("expanded.txt") { expand(mapOf("probe" to "platform:main")) }
        }
        developmentModSourceSet()
        // Persist paths as task inputs, avoiding Project/SourceSet access during execution.
        tasks.register("verifyRuntimeRoots") {
            val roots = objects.fileCollection().from(sets.named("main").get().runtimeClasspath)
            inputs.files(roots)
            val expected = layout.buildDirectory.dir("mdk/developmentMod").get().asFile
            doLast {
                check(roots.files == setOf(expected)) { "Uncomposed mod roots: ${roots.files}" }
            }
        }
        tasks.named<JavaCompile>("compileJava") {
            doFirst { check(source.files.none { "/src/config" in it.path.replace(File.separatorChar, '/') }) }
        }
    }
}
'''


def enabled(target, owner, source_set):
    if owner != "platform":
        return source_set == "main" or (target != "disabled" and not (target == "optional" and owner == "version"))
    return source_set == "main" or (source_set == "config" and target != "disabled") or (
        source_set == "configClient" and target in ("neo", "fabric")
    ) or (source_set == "client" and target == "fabric")


def run(root, *tasks):
    subprocess.run([
        str(REPO / ("gradlew.bat" if os.name == "nt" else "gradlew")), "-p", str(root), *tasks,
        "--no-daemon", "--no-parallel", "--max-workers=1", "--configuration-cache",
        "-Dorg.gradle.jvmargs=-Xmx512m", "-Pkotlin.compiler.execution.strategy=in-process",
    ], check=True)


def verify(root, cases):
    for target in TARGETS:
        expected = {}
        for name, candidates in cases.items():
            present = [item for item in candidates if enabled(target, *item)]
            if present:
                expected[name] = ":".join(present[0]) + "\n"
        stage = root / target / "build/mdk/developmentMod"
        merged = root / target / "build/mdk/composedResources"
        with ZipFile(root / target / "build/libs" / f"{target}.jar") as runtime, ZipFile(
            root / target / "build/libs" / f"{target}-sources.jar"
        ) as sources:
            for archive in (runtime, sources):
                names = archive.namelist()
                assert len(names) == len(set(names)), (target, "duplicate archive entries")
                actual = {n: archive.read(n).decode() for n in names if n.endswith(".txt")}
                assert actual == expected, (target, actual, expected)
            for directory in (stage, merged):
                actual = {p.relative_to(directory).as_posix(): p.read_text() for p in directory.rglob("*.txt")}
                assert actual == expected, (target, directory, actual, expected)
            runtime_classes = {n[:-6] for n in runtime.namelist() if n.endswith(".class")}
            source_classes = {n[:-5] for n in sources.namelist() if n.endswith(".java")}
            staged_classes = {p.relative_to(stage).as_posix()[:-6] for p in stage.rglob("*.class")}
            assert runtime_classes == source_classes == staged_classes, target
        print(f"{target}: {len(expected)} resources, unique entries, sources and staged classes: PASS")


def main():
    with tempfile.TemporaryDirectory(prefix="mdk-resource-regression-") as temporary:
        root = Path(temporary)
        (root / "settings.gradle.kts").write_text(
            'rootProject.name = "resource-regression"\ninclude("common", "version", '
            + ", ".join(f'"{t}"' for t in TARGETS) + ")\n"
        )
        (root / "build.gradle.kts").write_text(BUILD)
        helper = root / "buildSrc/src/main/kotlin/net/meatwo310/mdk/build"
        helper.mkdir(parents=True)
        (root / "buildSrc/build.gradle.kts").write_text('plugins { `kotlin-dsl` }\nrepositories { mavenCentral() }\n')
        for name in ("SourceSetArtifacts.kt", "ConfigSourceSets.kt", "ResourceComposition.kt"):
            shutil.copy2(REPO / "buildSrc/src/main/kotlin/net/meatwo310/mdk/build" / name, helper / name)
        cases = {f"pair-{i}.txt": pair for i, pair in enumerate(combinations(INPUTS, 2))}
        cases["all.txt"] = INPUTS
        cases.update({f"unique-{owner}-{ss}.txt": ((owner, ss),) for owner, ss in INPUTS})
        for name in ("deleted.txt", "expanded.txt", "generated.txt"):
            cases[name] = (("platform", "main"),)
        cases["assets/examplemod/probe.txt"] = INPUTS
        cases["data/examplemod/probe.txt"] = INPUTS
        for name, candidates in cases.items():
            for owner, ss in candidates:
                projects = TARGETS if owner == "platform" else (owner,)
                for target in projects:
                    path = root / target / "src" / ss / "resources" / name
                    path.parent.mkdir(parents=True, exist_ok=True)
                    path.write_text(f"{owner}:{ss}\n")
        for target in TARGETS:
            (root / target / "src/main/resources/generated.txt").unlink()
            (root / target / "src/main/resources/expanded.txt").write_text("${probe}\n")
            generated = root / target / "templates/generated.txt"
            generated.parent.mkdir(parents=True, exist_ok=True)
            generated.write_text("${probe}\n")
        for owner, ss in INPUTS:
            for target in TARGETS if owner == "platform" else (owner,):
                class_name = f"{owner}_{ss}"
                path = root / target / "src" / ss / "java/fixture" / f"{class_name}.java"
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_text(f"package fixture; public class {class_name} {{}}\n")
        tasks = [f":{t}:{task}" for t in TARGETS for task in ("jar", "sourcesJar", "stageModForDevelopment", "verifyRuntimeRoots")]
        run(root, *tasks)
        verify(root, cases)
        run(root, *tasks)  # Configuration cache reuse.
        for target in TARGETS:
            (root / target / "src/main/resources/deleted.txt").unlink()
            for owner, source_set in INPUTS:
                if owner == "platform":
                    (root / target / "src" / source_set / "resources/all.txt").unlink()
        del cases["deleted.txt"]
        cases["all.txt"] = INPUTS[4:]  # Removing overrides restores the next eligible contributor.
        # Stage first and then archive: ensure dev runs do not contaminate subsequent jars.
        run(root, *[f":{t}:stageModForDevelopment" for t in TARGETS], *tasks)
        verify(root, cases)
        for name in ("common", "version"):
            assert not list((root / name / "build/libs").glob("*sources.jar"))
        print("Input deletion and fallback, stage -> jar, optional/disabled config, late sourcesJar, configuration cache: PASS")


if __name__ == "__main__":
    main()
