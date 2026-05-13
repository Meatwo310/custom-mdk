plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // Match the oldest supported loader classpath: 1.18.2 resolves slf4j-api to 1.8.0-beta4.
    compileOnly(libs.slf4j.api)
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(17)
}
