plugins {
    `java-library`
    idea
    id("net.neoforged.moddev.legacyforge")
}

val modId: String by project
val minecraftVersion: String by project

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

// Use a separate variable to avoid shadowing by LegacyForgeExtension.getMinecraftVersion()
val mcpVer = minecraftVersion
legacyForge {
    mcpVersion = mcpVer
}
