//plugins {
//    `java-platform`
//}

description = "Bill of Materials for mgenerate4j"
extra["displayName"] = "MGenerate BOM"

val log4jVersion = "2.13.3"

dependencies {
    constraints {
        api("org.mongodb:bson:3.11.2")
        api("org.junit.jupiter:junit-jupiter:5.5.2")
        api("org.assertj:assertj-core:3.14.0")
        api("org.slf4j:slf4j-api:1.7.25")
        runtime("org.apache.logging.log4j:log4j-api:$log4jVersion")
        runtime("org.apache.logging.log4j:log4j-core:$log4jVersion")
        runtime("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

        api(project(":mgenerate-core"))
        api(project(":mgenerate-apt"))
        api(project(":mgenerate-common"))
    }
}