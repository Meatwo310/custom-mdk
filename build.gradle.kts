import org.gradle.plugins.ide.idea.model.IdeaModel

plugins {
    id("net.fabricmc.fabric-loom") apply false
    id("net.fabricmc.fabric-loom-remap") apply false
    id("net.neoforged.moddev") apply false
    id("net.neoforged.moddev.legacyforge") apply false
}

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

with(System.getProperties()) {
    val version = get("java.version")
    val vmVersion = get("java.vm.version")
    val vendor = get("java.vendor")
    val arch = get("os.arch")
    println("Configuring with Java: $version, JVM: $vmVersion ($vendor), Arch: $arch")
}

subprojects {
    val modVersion: String by project
    val modGroupId: String by project

    version = modVersion
    group = modGroupId

    tasks.withType<AbstractArchiveTask>().configureEach {
        archiveVersion.set("v$modVersion")
    }

    repositories {
//        flatDir {
//            dir("libs")
//        }

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://cursemaven.com")
                }
            }
            filter {
                includeGroup("curse.maven")
            }
        }

        exclusiveContent {
            forRepository {
                maven {
                    name = "Modrinth"
                    url = uri("https://api.modrinth.com/maven")
                }
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }

        exclusiveContent {
            forRepository {
                maven {
                    name = "Fuzs Mod Resources"
                    url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
                }
            }
            filter {
                includeGroupByRegex("fuzs\\..+")
            }
        }

        maven {
            name = "ParchmentMC"
            url = uri("https://maven.parchmentmc.org")
        }

//        maven {
//            name = "ModMaven"
//            url = uri("https://modmaven.dev/")
//        }
    }

    tasks.withType<JavaCompile>().configureEach {
        doFirst {
            with(javaCompiler.get().metadata) {
                println("Compiling with Java: $javaRuntimeVersion, JVM: $jvmVersion ($vendor)")
            }
        }
        options.encoding = "UTF-8"
    }

    afterEvaluate {
        tasks.withType<JavaExec>().configureEach {
            standardInput = System.`in`
        }
    }

    plugins.withId("idea") {
        configure<IdeaModel> {
            module {
                isDownloadSources = true
                isDownloadJavadoc = true
            }
        }
    }
}
