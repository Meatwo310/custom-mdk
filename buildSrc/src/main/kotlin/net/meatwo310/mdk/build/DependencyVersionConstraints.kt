package net.meatwo310.mdk.build

import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency

fun pin(version: String): Action<ExternalModuleDependency> = Action {
    version {
        strictly(version)
    }
}

fun req(version: String): Action<ExternalModuleDependency> = Action {
    version {
        require(version)
    }
}
