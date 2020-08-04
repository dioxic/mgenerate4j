plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.21.0"
}

allprojects {
    group = "uk.dioxic.mgenerate"
    version = "0.0.6-SNAPSHOT"
//    java.sourceCompatibility = JavaVersion.VERSION_11
}

// The root project doesn't produce a JAR.
tasks["jar"].enabled = false

// Load the Sonatype user/password for use in publishing tasks.
val sonatypeUser: String? by project
val sonatypePassword: String? by project

/*
 * Sonatype Staging Finalization
 * ====================================================
 *
 * When publishing to Maven Central, we need to close the staging
 * repository and release the artifacts after they have been
 * validated. This configuration is for the root project because
 * it operates at the "group" level.
 */
//if (sonatypeUser != null && sonatypePassword != null) {
//    apply(plugin = "io.codearte.nexus-staging")
//
//    nexusStaging {
//        packageGroup = "uk.dioxic"
//        stagingProfileId = "mgenerate4j"
//
//        username = sonatypeUser
//        password = sonatypePassword
//    }
//}

subprojects {
    val subproject = this

    repositories {
        mavenCentral()
        jcenter()
    }

    /*
     * Java
     * ====================================================
     *
     * By default, build each subproject as a java library.
     * We can add if-statements around this plugin to change
     * how specific subprojects are built (for example, if
     * we build Sphinx subprojects with Gradle).
     */

    if (project.hasProperty("isPlatform")) {
        apply(plugin = "java-platform")
    } else {
        apply(plugin = "java-library")

        java {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        dependencies {
            // add logging dependencies
            implementation("org.slf4j:slf4j-api")
            runtimeOnly("org.apache.logging.log4j:log4j-api")
            runtimeOnly("org.apache.logging.log4j:log4j-core")
            runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")

            // add test framework dependencies
            testImplementation("org.junit.jupiter:junit-jupiter")
            testImplementation("org.assertj:assertj-core")
        }

        // Use Junit5's test runner.
        tasks.withType<Test> {
            useJUnitPlatform()
        }

        // Reusable license copySpec
        val licenseSpec = copySpec {
            from("${project.rootDir}/LICENSE")
//        from("${project.rootDir}/NOTICE")
        }

        // Set up tasks that build source and javadoc jars.
        tasks.register<Jar>("sourcesJar") {
            metaInf.with(licenseSpec)
            from(sourceSets.main.get().allJava)
            archiveClassifier.set("sources")
        }

        tasks.register<Jar>("javadocJar") {
            metaInf.with(licenseSpec)
            from(tasks.javadoc)
            archiveClassifier.set("javadoc")
        }

        tasks.jar {
            metaInf.with(licenseSpec)
            inputs.property("moduleName", subproject.extra["moduleName"])
            manifest {
                attributes["Automatic-Module-Name"] = subproject.extra["moduleName"]
            }
        }

        // Always run javadoc after build.
        tasks["build"].dependsOn(tasks["javadoc"])
    }

    /*
     * Maven
     * ====================================================
     *
     * Publish to Maven central.
     */

    apply(plugin = "maven-publish")
    apply(plugin = "signing")

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
                if (project.hasProperty("isPlatform")) {
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
                        name.set(subproject.extra["displayName"].toString())
                        description.set(subproject.description)
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

/*
 * Javadoc
 * ====================================================
 *
 * Configure javadoc to collect sources and construct
 * its classpath from the main sourceset of each
 * subproject.
 */
tasks.javadoc {
    title = "mgenerate4j ${project.version}"
    setDestinationDir(file("$buildDir/docs/javadoc/latest"))
    source(project.subprojects.map {
        if (!it.hasProperty("isPlatform")) {
            project(it.name).sourceSets.main.get().allJava
        }
    })
    classpath = files(project.subprojects.map {
        if (!it.hasProperty("isPlatform")) {
            project(it.name).sourceSets.main.get().compileClasspath
        }
    })
}