# Project Map

This file maps repository locations to responsibilities.

## Root

`settings.gradle.kts`

- Includes every Gradle subproject that is enabled in this repository.
- Defines `ciBuildProjectNames` as all children except `common` and `*-common`.
- Controls which loader projects enter the build and release matrices.

`build.gradle.kts`

- Applies loader Gradle plugins at the root with `apply false`.
- Registers `writeCiBuildMatrix`.
- Reads `version.txt` into `modVersion`.
- Sets common subproject group, version, repositories, Java compile encoding,
  archive version, and IDEA behavior.

`gradle.properties`

- Global mod metadata and Fabric entrypoint defaults.

`version.txt`

- Single release version used by all subprojects.

`gradle/libs.versions.toml`

- Shared plugin and dependency aliases.

## Shared Code

`common/src/main`

- Cross-version Java used by all loaders and Minecraft versions.

`common/src/config`

- Cross-version config model and declaration code.

`<mc>-common/src/main`

- Java shared by all loaders for one Minecraft version.
- Use this for API differences tied to a Minecraft version.

`<mc>-common/src/config`

- Version-specific config support.

## Loader Projects

`<mc>-fabric`

- Fabric loader entrypoint and Fabric-specific integration.
- Uses either `fabric-loom-mod-conventions` or
  `fabric-loom-remap-mod-conventions`.
- Usually applies `fabric-api-conventions` and `fabric-config-conventions`.

`<mc>-forge`

- Legacy Forge entrypoint, resources, mixin config, templates, and runtime deps.
- Uses `legacyforge-mod-conventions`.
- Applies `legacyforge-config-conventions` when config code is included.

`<mc>-neo`

- NeoForge entrypoint, resources, templates, and runtime deps.
- Uses `neoforge-mod-conventions`.
- Applies `neoforge-config-conventions` when config code is included.

## Source Sets

`src/main/java`

- Loader-specific main code.
- Loader entrypoints live here unless a convention says otherwise.

`src/client/java`

- Fabric client source set, including Fabric `ModClient`.

`src/config/java`

- Config implementation source set.

`src/configClient/java`

- Client config UI source set.

`src/main/resources`

- Static loader resources, such as mixin JSON files.

`src/main/templates`

- Loader metadata templates.
- Fabric: `fabric.mod.json`.
- Legacy Forge: `META-INF/mods.toml`.
- NeoForge: `META-INF/neoforge.mods.toml`.

`src/generated/resources`

- Generated data resources for Forge and NeoForge data runs.

## buildSrc

`buildSrc/src/main/kotlin/*conventions.gradle.kts`

- Precompiled Gradle convention plugins.

`common-config-conventions`

- Adds `src/config` to `common` and packages its output.

`legacyforge-common-conventions`, `neoforge-common-conventions`

- Configure one-version common projects for Legacy Forge or NeoForge tooling.

`legacyforge-mod-conventions`, `neoforge-mod-conventions`

- Configure runs, metadata generation, Java toolchain, archives, and jar
  contents for Forge and NeoForge.

`fabric-mod-conventions`

- Configures Fabric metadata generation, Java toolchain, archives, and jar
  contents.

`fabric-loom-mod-conventions`, `fabric-loom-remap-mod-conventions`

- Configure Loom, split client source sets, Minecraft dependencies, mappings,
  and shared source sets.

`fabric-api-conventions`

- Adds Fabric API dependency and game test setup where supported.

`*-config-conventions`

- Wire `src/config` and `src/configClient` into classpaths and jars.

`net/meatwo310/mdk/build`

- Kotlin helpers for source sets, CI runtime mod staging, Fabric metadata,
  dependency version actions, version support, and version catalog access.

`DependencyVersionConstraints.kt`

- Provides `req(version)` for `version { require(version) }` and `pin(version)`
  for `version { strictly(version) }`.

`CiRuntimeModsConvention.kt`

- Creates non-transitive `ciRuntimeMods` and `collectCiRuntimeMods`.
- Stages direct CI runtime-test jars under `<project>/build/ciRuntimeMods`.

## CI And Release

`.github/workflows/build.yml`

- Detects platform projects with `writeCiBuildMatrix`.
- Uploads jars and runtime mod jars, then runs game/server/runtime tests.

`.github/workflows/release.yml`

- Manually bumps or keeps `version.txt`.
- Builds all platform projects from the CI matrix.
- Collects jars, generates release notes, tags, and publishes a GitHub Release.
