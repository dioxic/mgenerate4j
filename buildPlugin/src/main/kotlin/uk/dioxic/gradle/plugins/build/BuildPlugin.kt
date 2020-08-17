package uk.dioxic.gradle.plugins.build

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
        val config: BuildConfig = extensions.create(
                "buildConfig", BuildConfig::class.java
        )

        afterEvaluate {
            val isLibrary = hasPlugin("java-library")
            val isPlatform = hasPlugin("java-platform")
//            project.plugins.forEach {
//                println(it)
//            }
            if (hasPlugin("java") && !isPlatform) {
                configureJava()

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

                if (hasPlugin("kotlin")) {
                    configureKotlin()
                    configureDokka()
                }
            }

            if (isLibrary) {
                configureJavaPackaging(
                        moduleName = requireNotNull(config.moduleName) { "moduleName missing!" })
            }

            if (config.publish && (isLibrary || isPlatform)) {
                configureSonatypePublishing(
                        displayName = requireNotNull(config.displayName) { "displayName missing!" },
                        isPlatform = isPlatform)
            }
        }
    }
}

