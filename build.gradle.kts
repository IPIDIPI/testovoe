plugins {
    kotlin("jvm") version "2.2.20"
    groovy
    id("io.qameta.allure") version "3.0.1"
    id("org.openapi.generator") version "7.18.0"
}

group = "dhapr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.groovy:groovy-all:4.0.24")
    implementation("org.junit.jupiter:junit-jupiter:5.10.1")
    implementation("io.rest-assured:rest-assured:6.0.0")
    implementation("org.wiremock:wiremock-standalone:3.4.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("org.assertj:assertj-core:3.27.7")

    testImplementation("org.spockframework:spock-core:2.4-groovy-5.0")

    implementation("io.qameta.allure:allure-junit5:2.32.0")
    testImplementation("io.qameta.allure:allure-spock2:2.24.0")
    implementation("io.qameta.allure:allure-rest-assured:2.24.0")

    implementation("net.javacrumbs.json-unit:json-unit-assertj:5.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
kotlin {
    jvmToolchain(23)
}

tasks.register("cleanAllure") {
    group = "verification"
    doLast {
        delete(
            layout.buildDirectory.dir("allure-results"),
            layout.buildDirectory.dir("allure-report"),
        )
    }
}