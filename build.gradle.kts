plugins {
    id("uk.dioxic.scm")
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