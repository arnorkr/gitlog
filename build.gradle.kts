plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-html-builder:2.3.7")
}

application { mainClass = "org.example.MainKt" }

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }