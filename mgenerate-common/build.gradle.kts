plugins {
    `java-library`
}

dependencies {
    implementation(platform(project(":mgenerate-platform")))
    implementation("org.mongodb:bson")
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")
}