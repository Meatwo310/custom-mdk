import net.meatwo310.mdk.build.VersionCatalogLibrary
import net.meatwo310.mdk.build.configureVersionCommonConfigSourceSet
import net.meatwo310.mdk.build.module
import net.meatwo310.mdk.build.versionCatalog

val forgeConfigApiPortVersion: String by project

plugins.withId("java-library") {
    val config = configureVersionCommonConfigSourceSet("neoforge-common-config-conventions")

    dependencies.add(
        config.compileOnlyConfigurationName,
        "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortCommonNeoForgeApi)}:$forgeConfigApiPortVersion"
    )
}
