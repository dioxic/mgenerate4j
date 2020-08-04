plugins {
    `java-library`
}

dependencies {
    annotationProcessor(project(":mgenerate-apt"))
    api(project(":mgenerate-common"))
    implementation(platform(project(":mgenerate-platform")))

    implementation("info.picocli:picocli:4.1.2")
    implementation("uk.dioxic.faker4j:faker4j:0.0.4")
    implementation("ch.rasc:bsoncodec:1.0.1")
    implementation("commons-codec:commons-codec:1.13")
    implementation("org.reflections:reflections:0.9.11")

    implementation("org.mongodb:bson")
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")
    testImplementation("org.junit.jupiter:junit-jupiter")
}