package net.meatwo310.mdk.build

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import javax.inject.Inject

abstract class FabricModMetadataExtension @Inject constructor(objects: ObjectFactory) {
    val depends: MapProperty<String, String> =
        objects.mapProperty(String::class.java, String::class.java).convention(emptyMap())

    fun depend(modId: String, versionRange: String) {
        depends.put(modId, versionRange)
    }
}
