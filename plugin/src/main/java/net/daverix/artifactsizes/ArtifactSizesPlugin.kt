/*
   Copyright 2017 David Laurell

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
