plugins {
    `maven-publish`
}

allprojects {
    group = "uk.dioxic.mgenerate"
    version = "0.0.6-SNAPSHOT"
//    java.sourceCompatibility = JavaVersion.VERSION_11
}

subprojects {

    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        jcenter()
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                afterEvaluate {
                    if (project.components.names.contains("java")) {
                        from(project.components["java"])
                    } else if (project.components.names.contains("javaPlatform")) {
                        from(project.components["javaPlatform"])
                    }
                }

                pom {
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
        repositories {
            val repoUsername: String? by project
            val repoPassword: String? by project
            maven {
                if (repoUsername != null && repoPassword != null) {
                    credentials {
                        username = repoUsername
                        password = repoPassword
                    }
                    url = uri(
                            if (version.toString().endsWith("-SNAPSHOT")) "https://oss.sonatype.org/content/repositories/snapshots"
                            else "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    )

                } else {
                    url = uri("$buildDir/repo")
                }
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = sourceCompatibility
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}