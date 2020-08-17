package uk.dioxic.gradle.plugins.build

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import uk.dioxic.gradle.Dependencies
import uk.dioxic.gradle.Plugins

typealias Deps = Dependencies

enum class LoggingImpl {
    LOG4J2
}

enum class LoggingApi {
    LOG4J2,
    SLF4J
}

val Project.java: JavaPluginExtension
    get() = extensions.getByName("java") as JavaPluginExtension

val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

val Project.publishing: PublishingExtension
    get() = extensions.getByName("publishing") as PublishingExtension

val Project.signing: SigningExtension
    get() = extensions.getByName("signing") as SigningExtension

internal fun Project.java(configure: Action<JavaPluginExtension>) =
        configure.execute(java)

internal fun Project.publishing(configure: Action<PublishingExtension>) =
        configure.execute(publishing)

internal fun Project.signing(configure: Action<SigningExtension>) =
        configure.execute(signing)

internal fun Project.configureLogging(apiOnly: Boolean = false, implementation: LoggingImpl = LoggingImpl.LOG4J2) {
    println("configuring Logging")

    // add slf4j api
    project.dependencies.add("implementation", Dependencies.slf4jApi)

    val impl = if (apiOnly) "testRuntimeOnly" else "runtimeOnly"

    // add logging implementation
    when (implementation) {
        LoggingImpl.LOG4J2 -> {
            project.dependencies.add(impl, Dependencies.log4jCore)
            project.dependencies.add(impl, Dependencies.log4jSlf4jImpl)
        }
    }

}

internal fun Project.hasPlugin(id: String) = pluginManager.hasPlugin(id)

internal fun Project.configureKotlin() {
//    println("configuring Kotlin")
//
//    tasks.withType<KotlinCompile> {
//        kotlinOptions.jvmTarget = "1.8"
//    }
}

internal fun Project.configureJava() {
    println("configuring Java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

internal fun Project.configureJavaPackaging(moduleName: String) {
    println("configuring Java Packaging")

    // Reusable license copySpec
    val licenseSpec = copySpec {
        from("${project.rootDir}/LICENSE")
    }

    // Set up tasks that build source and javadoc jars.
    tasks.register<Jar>("sourcesJar") {
//        metaInf.with(licenseSpec)
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(sourceSets["main"].allJava)
        archiveClassifier.set("sources")
    }

    tasks.register<Jar>("javadocJar") {
//        metaInf.with(licenseSpec)
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(tasks.withType(Javadoc::class))
        archiveClassifier.set("javadoc")
    }

    tasks.withType<Jar> {
        metaInf.with(licenseSpec)
        duplicatesStrategy = DuplicatesStrategy.FAIL
        inputs.property("moduleName", moduleName)
        manifest {
            attributes["Automatic-Module-Name"] = moduleName
        }
    }

    // Always run javadoc after build.
//    tasks["build"].dependsOn(tasks["javadoc"])
}

internal fun Project.fatJar(mainClass: String) {
    println("configuring Shadow")
    apply(plugin = Plugins.shadow.pluginId)

    tasks.withType<ShadowJar> {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to mainClass))
        }
    }
}

internal fun Project.configureTesting() {
    println("configuring Testing")

    dependencies {
        add("testImplementation", Dependencies.junit5)
        add("testImplementation", Dependencies.assertj)
    }

    // Use Junit5's test runner.
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

internal fun Project.configureDokka() {
//    println("configuring Dokka")
//    apply(plugin = Plugins.dokka.pluginId)
}

internal fun Project.configureSonatypePublishing(displayName: String, isPlatform: Boolean) {
    println("configuring Sonatype Publishing")

    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    // Load the Sonatype user/password for use in publishing tasks.
    val sonatypeUser: String? by project
    val sonatypePassword: String? by project

    publishing {
        repositories {
            maven {
                if (sonatypeUser != null && sonatypePassword != null) {
                    credentials {
                        username = sonatypeUser
                        password = sonatypePassword
                    }
                    url = uri(
                            if (version.toString().endsWith("-SNAPSHOT"))
                                "https://oss.sonatype.org/content/repositories/snapshots"
                            else
                                "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    )

                } else {
                    url = uri("$buildDir/repo")
                }
            }
        }

        publications {
            create<MavenPublication>("mavenJava") {
                if (isPlatform) {
                    from(components["javaPlatform"])
                } else {
                    from(components["java"])
                    // Ship the source and javadoc jars.
                    artifact(tasks["sourcesJar"])
                    artifact(tasks["javadocJar"])
                }

                // Include extra information in the POMs.
                afterEvaluate {
                    pom {
                        name.set(displayName)
                        description.set(project.description)
                        url.set("https://github.com/dioxic/mgenerate4j")
                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                distribution.set("repo")
                            }
                        }
                        scm {
                            connection.set("scm:git:git://github.com/dioxic/mgenerate4j.git")
                            developerConnection.set("scm:git:git@github.com:dioxic/mgenerate4j.git")
                            url.set("git://github.com/dioxic/mgenerate4j.git")
                        }
                        developers {
                            developer {
                                id.set("dioxic")
                                name.set("Mark Baker-Munton")
                                email.set("dioxic@gmail.com")
                            }
                        }
                    }
                }
            }
        }

        // Don't sign the artifacts if we didn't get a key and password to use.
        val signingKey: String? by project
        val signingPassword: String? by project

        if (signingKey != null && signingPassword != null) {
            signing {
                useInMemoryPgpKeys(signingKey, signingPassword)
                sign(publishing.publications["mavenJava"])
            }
        }
    }
}