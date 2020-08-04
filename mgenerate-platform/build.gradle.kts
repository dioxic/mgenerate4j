plugins {
    `java-platform`
}

dependencies {
    constraints {
        api("org.mongodb:bson:3.11.2")
        api("org.junit.jupiter:junit-jupiter:5.5.2")
        api("org.assertj:assertj-core:3.14.0")
        api("org.slf4j:slf4j-api:1.7.25")
        runtime("org.apache.logging.log4j:log4j-api:2.12.1")
        runtime("org.apache.logging.log4j:log4j-core:2.12.1")
        runtime("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")

        api(project(":mgenerate-core"))
        api(project(":mgenerate-apt"))
        api(project(":mgenerate-common"))
    }
}