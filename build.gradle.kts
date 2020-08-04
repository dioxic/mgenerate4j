plugins {
    `maven-publish`
}

allprojects {
    group = "uk.dioxic.mgenerate"
    version = "0.0.6-SNAPSHOT"
    apply(plugin = "maven-publish")
//    java.sourceCompatibility = JavaVersion.VERSION_11
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        jcenter()
    }

//    configurations.all {
//    }

    publishing {
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
                            if (version.toString().endsWith("-SNAPSHOT")) "https://repo.spring.io/libs-snapshot-local/"
                            else "https://repo.spring.io/libs-milestone-local/"
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

publishing {
    publications {
        create<MavenPublication>("common") {
            val project = project(":mgenerate-common")
            groupId = project.group as String
            artifactId = "mgenerate-common"

            from(project.components["java"])

            pom {
                name.set("Common")
            }
        }
    }
    publications {
        create<MavenPublication>("apt") {
            val project = project(":mgenerate-apt")
            groupId = project.group as String
            artifactId = "mgenerate-apt"

            from(project.components["java"])

            pom {
                name.set("Apt")
            }
        }
    }
    publications {
        create<MavenPublication>("core") {
            val project = project(":mgenerate-core")
            groupId = project.group as String
            artifactId = "mgenerate-core"

            from(project.components["java"])

            pom {
                name.set("Core")
            }
        }
    }
}
