description = "Bill of Materials for mgenerate4j"
extra["displayName"] = "MGenerate BOM"

val log4jVersion = "2.13.3"
val mongoVersion = "4.1.0"
val reactorVersion="3.3.8.RELEASE"

dependencies {
    constraints {
        // mongo
        api("org.mongodb:bson:$mongoVersion")
        api("org.mongodb:mongodb-driver-reactivestreams:$mongoVersion")
        api("org.mongodb:mongodb-driver-sync:$mongoVersion")

        // reactor
        api("io.projectreactor:reactor-core:$reactorVersion")
        api("io.projectreactor:reactor-test:$reactorVersion")

        // testing
        api("org.junit.jupiter:junit-jupiter:5.5.2")
        api("org.assertj:assertj-core:3.16.1")

        // logging
        api("org.slf4j:slf4j-api:1.7.25")
        api("org.apache.logging.log4j:log4j-api:$log4jVersion")
        runtime("org.apache.logging.log4j:log4j-core:$log4jVersion")
        runtime("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")



        api(project(":mgenerate-core"))
        api(project(":mgenerate-apt"))
        api(project(":mgenerate-common"))
        api(project(":mgenerate-cli"))
    }
}