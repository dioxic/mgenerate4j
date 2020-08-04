//plugins {
//    `java-library`
//}

description = "Core code for mgenerate4j"
extra["displayName"] = "Mgenerate Core"
extra["moduleName"] = "uk.dioxic.mgenerate.core"

dependencies {
    implementation(platform(project(":mgenerate-platform")))
    annotationProcessor(project(":mgenerate-apt"))
    api(project(":mgenerate-common"))

    implementation("org.mongodb:bson")

    implementation("info.picocli:picocli:4.1.2")
    implementation("uk.dioxic.faker4j:faker4j:0.0.4")
    implementation("ch.rasc:bsoncodec:1.0.1")
    implementation("commons-codec:commons-codec:1.13")
    implementation("org.reflections:reflections:0.9.11")
}