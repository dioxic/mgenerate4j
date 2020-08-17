import uk.dioxic.gradle.plugins.build.Deps

plugins {
    `java-library`
    id("uk.dioxic.build")
}

description = "Core code for mgenerate4j"

buildConfig {
    displayName = "Mgenerate Core"
    moduleName = "uk.dioxic.mgenerate.core"
    publish = true
}

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    annotationProcessor(project(":mgenerate-apt"))
    api(project(":mgenerate-common"))
    implementation(Deps.bson)
    implementation(Deps.faker4j)
    implementation(Deps.bsoncodec)
    implementation(Deps.commonsCodec)
    implementation(Deps.reflections)
}