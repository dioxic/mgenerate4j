package uk.dioxic.gradle.plugins.scm

import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import pl.allegro.tech.build.axion.release.domain.VersionConfig
import pl.allegro.tech.build.axion.release.domain.hooks.HookContext
import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import uk.dioxic.gradle.Plugins
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal val Project.scmVersion: VersionConfig
    get() = extensions["scmVersion"] as VersionConfig

internal fun Project.configureScm(githubUser: String,
                                  githubRepo: String,
                                  changelog: String,
                                  filesWithVersion: List<String>) {
    println("configuring Axion")
    apply(plugin = Plugins.axion.pluginId)

    val githubUrl = "https://github.com/$githubUser/$githubRepo"
    val currentDateString = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE)

    scmVersion.apply {
        repository.type = "git"
        repository.directory = project.rootProject.file("./")
        repository.remote = "origin"

        tag.prefix = "v"
        tag.versionSeparator = ""

        hooks(closureOf<HooksConfig> {
            filesWithVersion.forEach {
                pre("fileUpdate", mapOf(
                        "file" to it,
                        "pattern" to KotlinClosure2<String, HookContext, String>({ v, _ -> v }),
                        "replacement" to KotlinClosure2<String, HookContext, String>({ v, _ -> v })
                ))
            }

            pre("fileUpdate", mapOf(
                    "file" to changelog,
                    "pattern" to KotlinClosure2<String, HookContext, String>({ _, _ -> "## \\[Unreleased\\]" }),
                    "replacement" to KotlinClosure2<String, HookContext, String>({ v, _ ->
                        """
                        ## \[Unreleased\]

                        ## \[$v\] - $currentDateString
                    """.trimIndent()
                    })
            ))
            pre("fileUpdate", mapOf(
                    "file" to changelog,
                    "pattern" to KotlinClosure2<String, HookContext, String>({ _, _ -> "\\[Unreleased\\].+" }),
                    "replacement" to KotlinClosure2<String, HookContext, String>({ v, _ ->
                        """
                        \[Unreleased\]: $githubUrl\/compare\/${tag.prefix}${tag.versionSeparator}$v...HEAD
                        \[$v\]: $githubUrl\/releases\/tag\/${tag.prefix}${tag.versionSeparator}$v
                    """.trimIndent()
                    })
            ))
            pre("commit")
        })

    }
}