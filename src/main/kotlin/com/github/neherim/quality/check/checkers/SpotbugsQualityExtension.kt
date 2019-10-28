package com.github.neherim.quality.check.checkers

open class SpotbugsQualityExtension {
    var enabled: Boolean = true
    var toolVersion: String = "3.1.12"
    var excludeFilter: String = "config/spotbugs/spotbugs.xml"
    var ignoreFailures: Boolean = false
    var effort: String = "max"
    var reportLevel: String = "low"
    var include: List<String> = listOf("**/*.java")
    var exclude: List<String> = listOf()
    var reportFormat: SpotBugsReportFormat = SpotBugsReportFormat.XML
}

enum class SpotBugsReportFormat {
    HTML, XML, TEXT, EMACS
}