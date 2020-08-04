plugins {
    `java-library`
}

dependencies {
    api(platform(project(":mgenerate-platform")))
    api(project(":mgenerate-common"))
    implementation("com.squareup:javapoet:1.11.1")
    implementation("org.mongodb:bson")
}