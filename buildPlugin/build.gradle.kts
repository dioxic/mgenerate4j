plugins {
    `kotlin-dsl`
}

group = "uk.dioxic.build"
version = "1.0"

gradlePlugin {
    plugins {
        register("buildPlugin") {
            id = "uk.dioxic.build"
            implementationClass = "uk.dioxic.build.BuildPlugin"
        }
    }
}

val kotlinVersion = "1.4.0-rc"
val dokkaVersion = kotlinVersion

dependencies {
    implementation(kotlin("gradle-plugin", kotlinVersion))
    implementation("com.github.jengelman.gradle.plugins:shadow:6.0.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
}

kotlinDslPluginOptions.experimentalWarning.set(false)

repositories {
    mavenCentral()
    jcenter()
}