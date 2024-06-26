plugins {
    kotlin("jvm") version "1.9.23"
    id("application")
}

group = "org.computer.whunter.ocpp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

buildscript {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("eu.chargetime.ocpp:common:1.2.0")
    implementation("eu.chargetime.ocpp:OCPP-J:1.2.0")
    implementation("eu.chargetime.ocpp:v1_6:1.2.0")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-simple:2.0.13")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "org.computer.whunter.ocpp.MainKt"
}
