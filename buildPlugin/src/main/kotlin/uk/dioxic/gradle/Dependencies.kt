package uk.dioxic.gradle

object Dependencies {
    const val junit5 = "org.junit.jupiter:junit-jupiter:${Versions.junit5}"
    const val slf4jApi = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertj}"
    const val reactorCore = "io.projectreactor:reactor-core:${Versions.reactor}"
    const val reactorTest = "io.projectreactor:reactor-test:${Versions.reactor}"
    const val clikt = "com.github.ajalt:clikt:${Versions.clikt}"
    const val faker4j = "uk.dioxic.faker4j:faker4j:${Versions.faker4j}"
    const val reflections = "org.reflections:reflections:${Versions.reflections}"
    const val commonsCodec = "commons-codec:commons-codec:${Versions.commonsCodec}"
    const val bsoncodec = "ch.rasc:bsoncodec:${Versions.bsoncodec}"
    const val javapoet = "com.squareup:javapoet:${Versions.javapoet}"

    // plugins
    const val shadowPlugin = "com.github.jengelman.gradle.plugins:shadow:${Versions.shadowPlugin}"
    const val dokkaPlugin = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokkaPlugin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinPlugin}"
    const val kotlinPluginApi = "org.jetbrains.kotlin:kotlin-gradle-plugin-api:${Versions.kotlinPlugin}"

    // kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val kotlinCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"
    const val kotlinStatistics = "org.nield:kotlin-statistics:${Versions.kotlinStatistics}"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinSerialization}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"

    // mongo
    const val bson = "org.mongodb:bson:${Versions.mongo}"
    const val mongodbDriverSync = "org.mongodb:mongodb-driver-sync:${Versions.mongo}"
    const val mongodbDriverReactive = "org.mongodb:mongodb-driver-reactivestreams:${Versions.mongo}"

    // log4j
    const val log4jCore = "org.apache.logging.log4j:log4j-core:${Versions.log4j}"
    const val log4jSlf4jImpl = "org.apache.logging.log4j:log4j-slf4j-impl:${Versions.log4j}"

}