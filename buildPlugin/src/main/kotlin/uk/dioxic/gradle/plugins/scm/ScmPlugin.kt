package uk.dioxic.gradle.plugins.scm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import uk.dioxic.gradle.Plugins


class ScmPlugin : Plugin<Project> {

    open class ScmConfig(private val project: Project) {
        var fileUpdateVersion: List<String> = emptyList()
        var changelog = "CHANGELOG.md"
        var githubUser: String? = null
        var githubRepo: String? = null
        val version: String
            get() = project.scmVersion.version
    }

    override fun apply(project: Project): Unit = project.run {
        val config: ScmConfig = extensions.create(
                "scmConfig", ScmConfig::class.java, project
        )

        apply(plugin = Plugins.axion.pluginId)

        scmVersion.apply {
            repository.type = "git"
            repository.directory = rootProject.file("./")
            repository.remote = "origin"

            tag.prefix = "v"
            tag.versionSeparator = ""
        }

        val scmVersion = scmVersion.version

        allprojects {
            version = scmVersion
        }

        afterEvaluate {
            requireNotNull(config.githubUser) { "scmConfig.githubUser must be set!" }
            requireNotNull(config.githubRepo) { "scmConfig.githubRepo must be set!" }

            configureScm(
                    githubUser = config.githubUser!!,
                    githubRepo = config.githubUser!!,
                    changelog = config.changelog,
                    filesWithVersion = config.fileUpdateVersion
            )
        }
    }
}

