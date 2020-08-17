import uk.dioxic.gradle.plugins.build.Deps

plugins {
    `java-platform`
    id("uk.dioxic.build")
}

description = "Bill of Materials for mgenerate4j"

buildConfig {
    displayName = "Mgenerate BOM"
    publish = true
}

dependencies {
    constraints {
        api(Deps.bson)
        api(Deps.mongodbDriverReactive)
        api(Deps.mongodbDriverSync)

        api(project(":mgenerate-core"))
        api(project(":mgenerate-apt"))
        api(project(":mgenerate-common"))
    }
}