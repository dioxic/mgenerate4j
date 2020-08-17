import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    `kotlin-dsl`
}

group = "uk.dioxic.gradle"
version = "1.0"

gradlePlugin {
    plugins {
        register("buildPlugin") {
            id = "uk.dioxic.build"
            implementationClass = "uk.dioxic.gradle.plugins.build.BuildPlugin"
        }
        register("scmPlugin") {
            id = "uk.dioxic.scm"
            implementationClass = "uk.dioxic.gradle.plugins.scm.ScmPlugin"
        }
    }
}

val kotlinVersion = KotlinCompilerVersion.VERSION
val dokkaVersion = "0.10.1"
val axionVersion = "1.11.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.github.jengelman.gradle.plugins:shadow:6.0.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("pl.allegro.tech.build:axion-release-plugin:$axionVersion")
}

kotlinDslPluginOptions.experimentalWarning.set(false)

repositories {
    mavenCentral()
    jcenter()
}