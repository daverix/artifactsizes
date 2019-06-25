/*
   Copyright 2019 David Laurell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.daverix.artifactsizes

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.internal.component.AmbiguousVariantSelectionException
import org.gradle.kotlin.dsl.get

open class CalculateSizesTask : DefaultTask() {

    var configuration: String? = null
        @Option(option = "config", description = "the configuration to calculate artifact sizes from")
        set
        @Input
        @Optional
        get

    @TaskAction
    fun calculate() {
        val config = configuration
        if (config == null) {
            project.configurations
                    .filter { it.isCanBeResolved }
                    .forEach { it.calculateSizes() }
        } else {
            project.configurations[config].calculateSizes()
        }
    }

    private fun Configuration.calculateSizes() {
        println("Configuration $name:")

        try {
            val artifactSizes = findArtifactSizes()
                    .sortedByDescending { it.size }
            for (artifact in artifactSizes) {
                println("${artifact.size.toSizeString().padEnd(12)} ${artifact.filename} (${artifact.group}:${artifact.artifactId}:${artifact.version})")
            }

            println("Total: ${artifactSizes.map { it.size }.sum().toSizeString()}")
        } catch (ex: AmbiguousVariantSelectionException) {
            // See https://github.com/gradle/gradle/issues/5426
            println("Error resolving dependencies for config $name")
            logger.info("Cannot resolve dependencies for $name", ex)
        }
        println()
    }

    private fun Configuration.findArtifactSizes(): List<ArtifactSize> =
            allDependencies.flatMap { dep ->
                files(dep).map { file ->
                    ArtifactSize(size = file.length(),
                            filename = file.name,
                            group = dep.group ?: "unknown",
                            artifactId = dep.name,
                            version = dep.version ?: "not specified")
                }
            }

    private fun Long.toSizeString(): String {
        var size = toDouble()
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

    private data class ArtifactSize(val size: Long,
                                    val filename: String,
                                    val group: String,
                                    val artifactId: String,
                                    val version: String)
}