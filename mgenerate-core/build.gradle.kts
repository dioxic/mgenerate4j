
dependencies {
    annotationProcessor(project(":mgenerate-apt"))
    implementation(project(":mgenerate-common"))
    implementation("info.picocli:picocli:4.1.2")
    implementation("uk.dioxic.faker4j:faker4j:0.0.4")
    implementation("org.mongodb:bson:3.11.2")
    implementation("ch.rasc:bsoncodec:1.0.1")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.apache.logging.log4j:log4j-api:2.12.1")
    implementation("org.apache.logging.log4j:log4j-core:2.12.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
    implementation("commons-codec:commons-codec:1.13")
    implementation("org.reflections:reflections:0.9.11")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testImplementation("org.junit.platform:junit-platform-runner:1.5.2")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

description = "mgenerate-core"
