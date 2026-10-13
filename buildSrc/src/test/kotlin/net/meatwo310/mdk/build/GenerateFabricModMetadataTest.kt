package net.meatwo310.mdk.build

import groovy.json.JsonSlurper
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class GenerateFabricModMetadataTest {
    @get:Rule
    val temporary = TemporaryFolder()

    @Test
    fun `optional mixin files are registered and removed independently`() {
        val root = temporary.newFolder()
        val project = ProjectBuilder.builder().withProjectDir(root).build()
        val template = root.resolve("fabric.mod.json").apply { writeText("{}") }
        val resources = root.resolve("resources").apply { mkdirs() }
        val task = project.tasks.register("metadata", GenerateFabricModMetadata::class.java).get().apply {
            templateFile.set(template)
            outputFile.set(root.resolve("output.json"))
            defaultModMetadataJson.set("""{"id":"custommod"}""")
            mixinConfigFiles.from(project.fileTree(resources) {
                include("custommod.mixins.json", "custommod.client.mixins.json")
            })
        }
        fun metadata(): Map<*, *> {
            task.generate()
            return JsonSlurper().parse(task.outputFile.get().asFile) as Map<*, *>
        }

        resources.resolve("othermod.mixins.json").writeText("{}")
        assertFalse(metadata().containsKey("mixins"))
        val common = resources.resolve("custommod.mixins.json").apply { writeText("{}") }
        assertEquals(listOf("custommod.mixins.json"), metadata()["mixins"])
        resources.resolve("custommod.client.mixins.json").writeText("{}")
        val client = mapOf("config" to "custommod.client.mixins.json", "environment" to "client")
        assertEquals(listOf("custommod.mixins.json", client), metadata()["mixins"])
        common.delete()
        assertEquals(listOf(client), metadata()["mixins"])
        assertEquals("custommod", metadata()["id"])

        template.writeText("""{"mixins":["explicit.mixins.json"]}""")
        assertEquals(listOf("explicit.mixins.json"), metadata()["mixins"])
        template.writeText("""{"mixins":[]}""")
        assertEquals(emptyList<Any>(), metadata()["mixins"])
    }
}
