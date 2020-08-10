import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0-rc"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("org.jetbrains.dokka") version "1.4.0-rc"
}

description = "CLI for mgenerate4j"
extra["displayName"] = "Mgenerate CLI"
extra["moduleName"] = "uk.dioxic.mgenerate.cli"

val coroutinesVersion = "1.3.8-1.4.0-rc"

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation(project(":mgenerate-core"))

    implementation("org.mongodb:mongodb-driver-sync")
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")

    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("org.nield:kotlin-statistics:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
}

// Use Junit5's test runner.
tasks.withType<Test> {
    useJUnitPlatform()
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