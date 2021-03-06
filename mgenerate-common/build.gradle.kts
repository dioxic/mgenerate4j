plugins {
    `java-library`
    id("uk.dioxic.build")
}

description = "Common code for mgenerate4j"

buildConfig {
    displayName = "Mgenerate Common"
    moduleName = "uk.dioxic.mgenerate.common"
    publish = true
}

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation("org.mongodb:bson")
}