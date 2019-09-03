import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    `maven-publish`
    id("com.adarshr.test-logger") version "1.7.0"
    id("com.bmuschko.docker-remote-api") version "4.10.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.3.41")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.41")
    implementation(group = "org.jsoup", name = "jsoup", version = "1.11.3")
    implementation(group = "com.willowtreeapps.assertk", name = "assertk-jvm", version = "0.13")
    implementation(group = "net.sourceforge.htmlunit", name = "htmlunit", version = "2.35.0")
    testImplementation(group = "com.github.tomakehurst", name = "wiremock", version = "2.24.1")
    testImplementation(group = "org.testcontainers", name = "testcontainers", version = "1.11.2")
    testImplementation(group = "org.testcontainers", name = "junit-jupiter", version = "1.11.2")
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
    testImplementation(group = "org.slf4j", name = "log4j-over-slf4j", version = "1.7.26")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.5.1")
    testImplementation(group = "com.nhaarman.mockitokotlin2", name = "mockito-kotlin", version = "2.1.0")
}

configurations.all {
    resolutionStrategy {
        force("org.eclipse.jetty:jetty-io:9.2.26.v20180806")
    }
}

group = "it.skrape"
version = "0.6.1"
description = "skrape{it}"

java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            apiVersion = "1.3"
            languageVersion = "1.3"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

testlogger {
    setTheme("mocha")
    slowThreshold = 3000
}
