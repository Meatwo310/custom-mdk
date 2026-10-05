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
