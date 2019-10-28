package com.github.neherim.quality.check.checkers

import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.rules.TemporaryFolder
import java.io.File

class TestProject(private val directory: TemporaryFolder) {

    fun pluginSettings(settings: String) = apply {
        directory.newFile("build.gradle").writeText(
            """
          |plugins {
          |  id "java"
          |  id "com.github.neherim.quality.check"
          |}
          |$settings
          |repositories {
          |  mavenCentral()
          |}
          |""".trimMargin()
        )
    }

    fun file(path: String, content: String) = apply {
        val file = directory.root.resolve(path)
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    fun fileFromResource(path: String, resource: String) = apply {
        val file = directory.root.resolve(path)
        File(this.javaClass.getResource(resource).toURI()).copyTo(file)
    }

    fun successBuild(taskToRun: String) = apply {
        run(taskToRun).build()
    }

    fun failBuild(taskToRun: String, containsMessage: String) = apply {
        val buildResult = run(taskToRun).buildAndFail()
        assertThat(buildResult.output, buildResult.output, CoreMatchers.containsString(containsMessage))
    }

    fun reportContains(reportFile: String, containsMessage: String) {
        val reportFileText = directory.root.resolve(reportFile).readText()
        assertThat(reportFileText, CoreMatchers.containsString(containsMessage))
    }

    private fun run(taskToRun: String) =
        GradleRunner.create().withPluginClasspath().withProjectDir(directory.root).withArguments(
            taskToRun,
            "--stacktrace"
        )
}