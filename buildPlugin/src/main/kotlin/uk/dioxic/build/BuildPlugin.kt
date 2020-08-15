package uk.dioxic.build

import org.gradle.api.Plugin
import org.gradle.api.Project


class BuildPlugin : Plugin<Project> {

    open class BuildConfig {
        var displayName: String? = null
        var moduleName: String? = null
        var mainClass: String? = null
        var publish: Boolean = false
        var testable: Boolean = true
        var logging: Boolean = true
        var loggingApi: LoggingApi = LoggingApi.SLF4J
        var loggingImplementation: LoggingImpl = LoggingImpl.LOG4J2
    }

    override fun apply(project: Project): Unit = project.run {
        val config: BuildConfig = project.extensions.create(
                "buildConfig", BuildConfig::class.java
        )


        project.afterEvaluate {
            val isLibrary = project.hasPlugin("java-library")
            val isPlatform = project.hasPlugin("java-platform")
//            project.plugins.forEach {
//                println(it)
//            }
            if (!isPlatform) {
                if (config.testable) {
                    configureTesting()
                }

                if (config.logging) {
                    configureLogging(
                            apiOnly = isLibrary,
                            implementation = config.loggingImplementation)
                }

                if (config.mainClass != null) {
                    fatJar(config.mainClass!!)
                }

                if (project.hasPlugin("kotlin")) {
                    configureKotlin()
                    configureDokka()
                }

                if (project.hasPlugin("java")) {
                    configureJava()
                }
            }

            if (isLibrary) {
                configureJavaPackaging(
                        moduleName = requireNotNull(config.moduleName) { "moduleName missing!" })
            }

            if (config.publish) {
                configureSonatypePublishing(
                        displayName = requireNotNull(config.displayName) { "displayName missing!" },
                        isPlatform = isPlatform)
            }
        }


    }
}

