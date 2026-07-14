import net.meatwo310.mdk.build.*

val minecraftVersion = project.property("minecraftVersion").toString()
val forgeConfigApiPortVersion = project.property("forgeConfigApiPortVersion").toString()

disallowConfigConvention("fabric-config-conventions", "fabric-legacy-config-conventions")
configureFabricConfigConvention(
    minecraftVersion,
    forgeConfigApiPortVersion,
    "fabric-legacy-config-conventions",
    VersionCatalogLibrary.ForgeConfigApiPortFabricLegacy,
    VersionConfigSourceSet.OPTIONAL,
)

plugins.withId("java-library") {
    // FCAP 3.x/4.x publishes Night Config as runtime-only even though ForgeConfigSpec exposes its types.
    dependencies.add("compileOnly", versionCatalog.library(VersionCatalogLibrary.NightConfigCoreLegacy))
}
