# custom-mdk template guide

A Minecraft mod template designed for multi-version & multi-loader development, powered by a single Gradle multi-project build. Everything is preconfigured out of the box: Kotlin DSL, Mixin support, Parchment mappings, automatic Java toolchain management, versioned artifact naming, and CI/CD workflows.

## Supported Platforms

| Minecraft | Fabric | Legacy Forge | NeoForge |
|-----------|:------:|:------------:|:--------:|
| 1.18.2    |   ⏳    |      ✅       |    -     |
| 1.19.2    |   ⏳    |      ✅       |    -     |
| 1.20.1    |  🚧*2  |      ✅       |    ❌     |
| 1.21.1    |  🚧*2  |      ❌       |    ✅     |
| 1.21.8    |  🚧*2  |      ⏳       |    ❌     |
| 1.21.11   |  🚧*2  |      ⏳       |    ❌     |
| 26.1      |   ✅    |      ❌       |    ✅     |

✅ Supported | 🚧 Partial support | ⏳ Planned | ❌ Not supported

*2 Game Test is not currently supported

## Setup

1. Click **"Use this template"** on GitHub to create your repository from this template
2. Select the platforms you need in `settings.gradle.kts` by removing unused `include(...)` lines to reduce Gradle's configuration and load time
3. Open in IntelliJ IDEA, then edit `gradle.properties` to set your mod's metadata (and fabric entry points)
4. Create a `README.md` and `LICENSE` **in the repository root**. [Open source licenses](https://opensource.org/licenses) are recommended
   * Leave `docs/*` unchanged if you want to stay in sync with upstream updates

## Receiving Upstream Updates

You can receive updates manually by adding this repository as a remote:

```sh
# Run once after creating your repository
git remote add upstream https://github.com/Meatwo310/custom-mdk.git
```

Then, whenever you want to pull in new changes:

```sh
git fetch upstream
git merge upstream/main
```

Resolve any conflicts as needed.

## Building

Build all platforms:

```sh
./gradlew build
```

Build a specific platform:

```sh
./gradlew :26.1-fabric:build
./gradlew :26.1-neo:build
```

## Running

```sh
./gradlew :1.18.2-forge:runClient
./gradlew :1.19.2-forge:runClient
./gradlew :1.20.1-forge:runClient
./gradlew :1.21.1-neo:runClient
./gradlew :26.1-fabric:runClient
./gradlew :26.1-neo:runClient
```

## Requirements

- JDK 17+ to configure Gradle (Temurin 25 recommended)
- The Java version used to compile and run the game is automatically downloaded per Minecraft version via [Foojay Toolchain Resolver](https://github.com/gradle/foojay-toolchains) — no manual installation needed

## Template License

[MIT](https://github.com/Meatwo310/custom-mdk/blob/main/TEMPLATE-LICENSE) - feel free to relicense your project
