import net.meatwo310.mdk.build.FabricModMetadataExtension
import net.meatwo310.mdk.build.VersionCatalogLibrary
import net.meatwo310.mdk.build.module
import net.meatwo310.mdk.build.versionCatalog

val forgeConfigApiPortVersion: String by project

plugins.withId("fabric-mod-conventions") {
    extensions.configure<FabricModMetadataExtension>("fabricModMetadata") {
        depend("forgeconfigapiport", ">=$forgeConfigApiPortVersion")
    }
}

plugins.withId("net.fabricmc.fabric-loom") {
    val dependency = "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion"
    dependencies.add("implementation", dependency)
    dependencies.add("runtimeMods", dependency)
}

plugins.withId("net.fabricmc.fabric-loom-remap") {
    val dependency = "${versionCatalog.module(VersionCatalogLibrary.ForgeConfigApiPortFabric)}:$forgeConfigApiPortVersion"
    dependencies.add("modImplementation", dependency)
    dependencies.add("runtimeMods", dependency)
}
