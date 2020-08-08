import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    kotlin("jvm") version "1.4.0-rc"
}

description = "CLI for mgenerate4j"
extra["displayName"] = "Mgenerate CLI"
extra["moduleName"] = "uk.dioxic.mgenerate.cli"

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation(project(":mgenerate-core"))

    implementation("org.mongodb:mongodb-driver-sync")
    implementation("io.projectreactor:reactor-core")
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")

    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8-1.4.0-rc")

    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("shadow")
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to "uk.dioxic.mgenerate.cli.CliKt"))
    }
}