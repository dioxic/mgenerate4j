package uk.dioxic.build

object Plugins {

    data class PluginDefinition(val pluginId: String, val groupId: String, val artifactId: String)

    val shadow = PluginDefinition("com.github.johnrengelman.shadow", "com.github.jengelman.gradle.plugins", "shadow")
    val dokka = PluginDefinition("org.jetbrains.dokka","org.jetbrains.dokka", "dokka-gradle-plugin")

}