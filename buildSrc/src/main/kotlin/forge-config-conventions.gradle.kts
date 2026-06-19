import net.meatwo310.mdk.build.requireConfigSourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType

plugins.withId("java-library") {
    extensions.getByType<SourceSetContainer>().named("main") {
        java.srcDirs(requireConfigSourceSet(":common", "forge-config-conventions").java.srcDirs)
        java.srcDir("src/config/java")
        resources.srcDir("src/config/resources")
        compileClasspath += requireConfigSourceSet(":common", "forge-config-conventions").output
    }
}
