import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "com.alexandria"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val versions =
    mapOf(
        "mockk" to "1.13.8",
        "faker" to "1.15.0",
        "rest-assured" to "5.3.2",
        "archunit-junit5" to "1.2.0",
        "spring-cloud-aws" to "3.0.3",
        "springmockk" to "4.0.2",
    )

dependencies {
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:${versions["spring-cloud-aws"]}"))

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("software.amazon.awssdk:s3-transfer-manager")
    implementation("software.amazon.awssdk.crt:aws-crt")
    implementation("org.yaml:snakeyaml:2.2")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:${versions["rest-assured"]}")
    testImplementation("io.mockk:mockk:${versions["mockk"]}")
    testImplementation("io.github.serpro69:kotlin-faker:${versions["faker"]}")
    testImplementation("com.tngtech.archunit:archunit-junit5:${versions["archunit-junit5"]}")
    testImplementation("com.ninja-squad:springmockk:${versions["springmockk"]}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

/**
 * Why do this to yourself?
 * - Enables to selectively run categories of tests. Cuts unit tests feedback loop down to milliseconds locally (instead of about 1 minute)
 * - Help cutting GitHub actions CI costs by not running long-running tests if the unit tests fail.
 */
val unitTest =
    tasks.register<Test>("unitTest") {
        useJUnitPlatform {
            filter {
                includeTestsMatching("*Test")
                excludeTestsMatching("*IntegrationTest")
                excludeTestsMatching("ArchitectureTest")
                excludeTestsMatching("*E2ETest")
            }
        }
    }

val integrationTest =
    tasks.register<Test>("integrationTest") {
        dependsOn("unitTest")
        useJUnitPlatform {
            filter {
                includeTestsMatching("*IntegrationTest")
            }
        }
    }

val architectureTest =
    tasks.register<Test>("architectureTest") {
        dependsOn("integrationTest")
        useJUnitPlatform {
            filter {
                includeTestsMatching("ArchitectureTest")
            }
        }
    }

val e2eTest =
    tasks.register<Test>("E2ETest") {
        dependsOn("architectureTest")
        useJUnitPlatform {
            filter {
                includeTestsMatching("*E2ETest")
            }
        }
    }

val tests =
    tasks.named("test") {
        dependsOn(unitTest, integrationTest, e2eTest, architectureTest)
    }

tasks.named("check") {
    dependsOn(tests)
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
