package net.daverix.artifactsizes

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class ArtifactSizesPlugin : Plugin<Project> {
    private val Project.resolvableConfigurations
        get() = configurations.filter { it.isCanBeResolved }

    override fun apply(project: Project) {
        with(project) {
            val parentTask = task("artifactSizes")

            afterEvaluate {
                resolvableConfigurations.forEach { config ->
                    val task = task("artifactSizes${config.name.capitalize()}").doLast {
                        config.sortedByDescending { it.length() }
                                .map { "${it.getSize().padEnd(12)}${it.name}" }
                                .distinct()
                                .forEach { println(it) }

                        println()
                    }
                    parentTask.dependsOn(task)
                }
            }
        }
    }

    private fun File.getSize(): String {
        var size: Double = length().toDouble()

        var suffixPower = 0
        while (size > 1024) {
            size /= 1024
            suffixPower++
        }

        val suffix = when (suffixPower) {
            4 -> "TB"
            3 -> "GB"
            2 -> "MB"
            1 -> "KB"
            else -> "bytes"
        }

        if (suffixPower > 0)
            size = (size * 100).toInt() / 100.0

        return "$size $suffix"
    }
}
