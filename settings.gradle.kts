pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "mgenerate"
include("mgenerate-common")
include("mgenerate-apt")
include("mgenerate-core")
include("mgenerate-bom")
include("mgenerate-cli")

includeBuild("buildPlugin")