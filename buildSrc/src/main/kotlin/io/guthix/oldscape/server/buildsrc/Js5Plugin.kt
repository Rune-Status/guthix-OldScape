/**
 * This file is part of Guthix OldScape.
 *
 * Guthix OldScape is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix OldScape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
package io.guthix.oldscape.server.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.language.jvm.tasks.ProcessResources

@Suppress("UnstableApiUsage")
class Js5Plugin : Plugin<Project> {
    override fun apply(target: Project) {
        val processResourceTask = target.getTasksByName("processResources", false).first()
        if(processResourceTask is ProcessResources) {
            processResourceTask.exclude("/cache/*")
        } else {
            throw IllegalStateException("Could not find processResources task in gradle project ${target.name}.")
        }
        val compileCache = target.tasks.register("compileCache", CompileCacheTask::class.java).get()
        val build = target.getTasksByName("build", false).first()
        build.dependsOn(compileCache)
    }
}