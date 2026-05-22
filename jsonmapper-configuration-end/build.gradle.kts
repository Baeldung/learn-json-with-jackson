plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.tools.jackson.core.jackson.databind)
    testImplementation(libs.org.junit.jupiter.junit.jupiter)
    testRuntimeOnly(libs.org.junit.platform.junit.platform.launcher)
}

group = "com.baeldung"
version = "1.0.0"
description = "jsonmapper-configuration-end"

java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
