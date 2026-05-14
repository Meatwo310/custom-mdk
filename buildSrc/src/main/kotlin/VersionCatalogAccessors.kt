import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

enum class VersionCatalogLibrary(val alias: String) {
    FabricApi("fabric-api"),
    FabricLoader("fabric-loader"),
    Minecraft("minecraft"),
    Mixin("mixin"),
    ParchmentData("parchment-data"),
}

enum class VersionCatalogVersion(val alias: String) {
    FabricLoader("fabric-loader"),
    Mixin("mixin"),
}

fun VersionCatalog.library(library: VersionCatalogLibrary): Provider<MinimalExternalModuleDependency> =
    findLibrary(library.alias).orElseThrow {
        IllegalArgumentException("Version catalog library '${library.alias}' is not defined.")
    }

fun VersionCatalog.module(library: VersionCatalogLibrary): String =
    library(library).get().module.toString()

fun VersionCatalog.version(version: VersionCatalogVersion): String =
    findVersion(version.alias).orElseThrow {
        IllegalArgumentException("Version catalog version '${version.alias}' is not defined.")
    }.requiredVersion
