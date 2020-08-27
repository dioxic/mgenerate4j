plugins {
    id("uk.dioxic.scm")
    id("com.github.ben-manes.versions") version "0.29.0"
}

scmConfig {
    githubUser = "dioxic"
    githubRepo = "mgenerate4j"
    fileUpdateVersion = listOf("README.md", "docs/quickstart.md")
}

allprojects {
    group = "uk.dioxic.mgenerate"
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}