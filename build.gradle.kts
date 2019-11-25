@file:Suppress("UNUSED_VARIABLE")

import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    kotlin("jvm") version "1.2.50"
    jacoco
    id("org.jetbrains.dokka") version "0.10.0"
    id("com.adarshr.test-logger") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.1.1"
}

val ideaDetected = System.getProperty("idea.version") != null

testlogger {
    setTheme(if (!ideaDetected) "mocha" else "standard")
    slowThreshold = 1000
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("net.sourceforge.htmlunit:htmlunit:2.36.0")
    implementation("io.github.rybalkinsd:kohttp:0.11.1")
    implementation("org.jsoup:jsoup:1.11.3")
    implementation("com.willowtreeapps.assertk:assertk-jvm:0.13")
    implementation("io.strikt:strikt-core:0.22.2")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.50")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.2.50")


    testImplementation("org.testcontainers:testcontainers:1.11.2")
    testImplementation("org.testcontainers:junit-jupiter:1.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.25.0")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("io.mockk:mockk:1.9.3.kotlin12")
    testImplementation("io.mockk:mockk-dsl-jvm:1.9.3.kotlin12")
    testImplementation("org.slf4j:log4j-over-slf4j:1.7.26")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

detekt {
    toolVersion = "1.1.1"
    input = files(DetektExtension.DEFAULT_SRC_DIR_KOTLIN)
    config = files("$projectDir/src/test/resources/detekt.yml")
}

tasks {
    jacocoTestReport {
        reports {
            xml.isEnabled = true
        }
    }

    test {
        dependsOn(detekt)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        finalizedBy(jacocoTestReport)
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

