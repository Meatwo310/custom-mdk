import net.meatwo310.mdk.build.*

val minecraftVersion = project.property("minecraftVersion").toString()
val forgeConfigApiPortVersion = project.property("forgeConfigApiPortVersion").toString()

disallowConfigConvention("fabric-legacy-config-conventions", "fabric-config-conventions")
configureFabricConfigConvention(
    minecraftVersion,
    forgeConfigApiPortVersion,
    "fabric-config-conventions",
    VersionCatalogLibrary.ForgeConfigApiPortFabric,
    VersionConfigSourceSet.REQUIRED,
)
