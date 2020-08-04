//plugins {
//    `java-library`
//}

description = "Annotation processing"
extra["displayName"] = "Mgenerate APT"
extra["moduleName"] = "uk.dioxic.mgenerate.apt"

dependencies {
    implementation(platform(project(":mgenerate-platform")))
    api(project(":mgenerate-common"))
    implementation("com.squareup:javapoet:1.11.1")
    implementation("org.mongodb:bson")
}