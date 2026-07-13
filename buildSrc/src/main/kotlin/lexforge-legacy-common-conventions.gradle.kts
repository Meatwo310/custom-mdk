plugins {
    `java-library`
    idea
    id("net.neoforged.moddev.legacyforge")
}

val modId = project.property("modId").toString()
val minecraftVersion = project.property("minecraftVersion").toString()

dependencies {
    api(project(":common"))
}

base {
    archivesName = "$modId-$minecraftVersion-common"
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(17)
    @Suppress("UnstableApiUsage")
    vendor = JvmVendorSpec.JETBRAINS
}

// Use a separate variable to avoid shadowing by ModDevGradle's LegacyForgeExtension.
val mcpVer = minecraftVersion
legacyForge {
    mcpVersion = mcpVer
}
