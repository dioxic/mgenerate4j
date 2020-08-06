description = "Common code for mgenerate4j"
extra["displayName"] = "MGenerate Common"
extra["moduleName"] = "uk.dioxic.mgenerate.common"

dependencies {
    implementation(platform(project(":mgenerate-bom")))
    implementation("org.mongodb:bson")
}