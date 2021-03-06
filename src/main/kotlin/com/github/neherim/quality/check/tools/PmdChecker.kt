package com.github.neherim.quality.check.tools

import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import java.io.File

object PmdChecker {
    fun addPlugin(root: Project, target: Project, ext: PmdQualityExtension) {
        if (ext.enabled) {
            target.plugins.apply(PmdPlugin::class.java)
            val ruleSetFile = root.file(ext.ruleSetFile)
            if (!ruleSetFile.exists()) {
                createDefaultRuleSet(ruleSetFile)
            }
            target.extensions.configure(PmdExtension::class.java) {
                it.toolVersion = ext.toolVersion
                it.isIgnoreFailures = ext.ignoreFailures
                it.isConsoleOutput = ext.consoleOutput
                it.ruleSetFiles = root.files(ruleSetFile)
                it.ruleSets = listOf()
            }

            target.tasks.withType(Pmd::class.java) {
                it.source = target.fileTree(ext.source)
                it.exclude(ext.exclude)
                it.include(ext.include)
                it.reports.html.isEnabled = ext.htmlReport
                it.reports.xml.isEnabled = ext.xmlReport
            }
        }
    }

    private fun createDefaultRuleSet(file: File) {
        file.parentFile.mkdirs()
        PmdChecker.javaClass.getResourceAsStream("/pmd-default-ruleset.xml")?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}