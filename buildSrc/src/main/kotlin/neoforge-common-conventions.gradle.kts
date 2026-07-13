plugins {
    `java-library`
    idea
    id("net.neoforged.moddev")
}

val modId = project.property("modId").toString()
val minecraftVersion = project.property("minecraftVersion").toString()
val javaVersion = project.property("javaVersion").toString()
val neoFormVer = project.property("neoFormVer").toString()

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
