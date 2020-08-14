import uk.dioxic.build.Deps

plugins {
    kotlin("jvm") version "1.4.0-rc"
    id("uk.dioxic.build") version "1.0"
}

description = "CLI for mgenerate4j"
extra["displayName"] = "Mgenerate CLI"
extra["moduleName"] = "uk.dioxic.mgenerate.cli"

buildConfig {
    displayName = "Mgenerate CLI"
    moduleName = "uk.dioxic.mgenerate.cli"
    mainClass = "uk.dioxic.mgenerate.cli.CliKt"
}

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation(project(":mgenerate-core"))

    implementation(Deps.mongodbDriverSync)
    implementation(Deps.clikt)
    implementation(Deps.kotlinStatistics)
    implementation(Deps.kotlinCoroutines)

    testImplementation(Deps.kotlinCoroutinesTest)
}

val coroutinesVersion = "1.3.8-1.4.0-rc"