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
