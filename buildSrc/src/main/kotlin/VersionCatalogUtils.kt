import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow {
        IllegalArgumentException("Version catalog library '$alias' is not defined.")
    }

fun VersionCatalog.module(alias: String): String =
    library(alias).get().module.toString()

fun VersionCatalog.version(alias: String): String =
    findVersion(alias).orElseThrow {
        IllegalArgumentException("Version catalog version '$alias' is not defined.")
    }.requiredVersion
