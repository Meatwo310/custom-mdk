package net.meatwo310.mdk.build

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

enum class VersionCatalogLibrary(val alias: String) {
    FabricApi("fabric-api"),
    FabricLoader("fabric-loader"),
    ForgeConfigApiPortCommon("forge-config-api-port-common"),
    ForgeConfigApiPortCommonNeoForgeApi("forge-config-api-port-common-neoforge-api"),
    ForgeConfigApiPortFabric("forge-config-api-port-fabric"),
    Minecraft("minecraft"),
    Mixin("mixin"),
    ParchmentData("parchment-data"),
}

enum class VersionCatalogVersion(val alias: String) {
    FabricLoader("fabric-loader"),
    Mixin("mixin"),
}

val Project.versionCatalog: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

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
