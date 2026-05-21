plugins {
    `java-library`
    idea
    id("net.neoforged.moddev")
}

val modId: String by project
val minecraftVersion: String by project
val javaVersion: String by project
val neoFormVer: String by project

dependencies {
    api(project(":common"))
}

base {
    archivesName = "$modId-$minecraftVersion-common"
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(javaVersion.toInt())
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

neoForge {
    neoFormVersion = neoFormVer
}
