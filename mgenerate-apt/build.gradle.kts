import uk.dioxic.gradle.plugins.build.Deps

plugins {
    `java-library`
    id("uk.dioxic.build")
}

description = "Annotation processing"

buildConfig {
    displayName = "Mgenerate APT"
    moduleName = "uk.dioxic.mgenerate.apt"
    publish = true
}

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    api(project(":mgenerate-common"))
    implementation(Deps.javapoet)
    implementation(Deps.bson)
}