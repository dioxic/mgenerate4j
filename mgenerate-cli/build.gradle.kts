description = "CLI for mgenerate4j"
extra["displayName"] = "Mgenerate CLI"
extra["moduleName"] = "uk.dioxic.mgenerate.cli"

plugins {
//    application
    id("io.freefair.lombok") version "5.1.0"
    kotlin("jvm") version "1.4.0-rc"
}

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation(project(":mgenerate-core"))

    implementation("org.mongodb:mongodb-driver-reactivestreams")
    implementation("io.projectreactor:reactor-core")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")

    implementation("info.picocli:picocli:4.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")

    testImplementation("io.projectreactor:reactor-test")
}