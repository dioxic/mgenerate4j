import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import uk.dioxic.gradle.plugins.build.Deps

plugins {
    kotlin("jvm") version "1.4.21"
    id("uk.dioxic.build")// version "1.0"
}

description = "CLI for mgenerate4j"

buildConfig {
    displayName = "Mgenerate CLI"
    moduleName = "uk.dioxic.mgenerate.cli"
    publish = false
    mainClass = "uk.dioxic.mgenerate.cli.CliKt"
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}

val coroutinesVersion = "1.3.8-1.4.0-rc"

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation(project(":mgenerate-core"))

    implementation(Deps.mongodbDriverSync)
    implementation(Deps.clikt)
    implementation(Deps.kotlinStatistics)
    implementation(Deps.kotlinCoroutines)

    testImplementation(Deps.kotlinCoroutinesTest)
    implementation(kotlin("stdlib-jdk8"))

}
repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}