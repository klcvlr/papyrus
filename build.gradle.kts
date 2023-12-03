import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import java.io.ByteArrayOutputStream


plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "com.alexandria"
version = getVersion()

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
        "logstash-logback-encoder" to "7.4",
        "snakeyaml" to "2.2",
        "springdoc-openapi-starter-webmvc-ui" to "2.2.0",
    )

configure<SpringBootExtension> {
    // WARNING - this adds the build timestamp in the build-info.properties file. This changes the artifact hash for every build.
    // This breaks the buildpacks ability of reproductible docker images (with same id) as long as the code doesn't change.
    // Switching to commit timestamp could be a good trade-off.
    buildInfo()
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:${versions["spring-cloud-aws"]}"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("software.amazon.awssdk:s3-transfer-manager")
    implementation("software.amazon.awssdk.crt:aws-crt")
    implementation("org.yaml:snakeyaml:${versions["snakeyaml"]}")
    implementation("net.logstash.logback:logstash-logback-encoder:${versions["logstash-logback-encoder"]}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["springdoc-openapi-starter-webmvc-ui"]}")

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

tasks.named("check") {
    dependsOn(tests)
}

tasks.bootBuildImage {
    dependsOn(tests)
    builder.set("paketobuildpacks/builder-jammy-base:latest")
    val containerRegistry = System.getenv("CONTAINER_REGISTRY")
    if (containerRegistry.isNullOrBlank()) {
        // WARNING - we Should probably just error out here and not allow the build to continue
        println("[WARNING] CONTAINER_REGISTRY environment variable is not set. Using local docker registry.")
        imageName.set("papyrus:${project.version}")
    } else {
        imageName.set("$containerRegistry/papyrus:${project.version}")
    }
}

/**
 * TEST TASKS CONFIGURATION
 *
 * Why do this to yourself?
 * - Enables to selectively run categories of tests. Cuts unit tests feedback loop down to milliseconds locally (instead of about 1 minute)
 * - Help cutting GitHub actions CI costs by not running long-running tests if the unit tests fail.
 */

val tests =
    tasks.named("test") {
        dependsOn(unitTest, integrationTest, e2eTest, architectureTest)
    }

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

// ---------------- UTILS ----------------
fun getCommitHash(): String {
    return ByteArrayOutputStream().use { outputStream ->
        project.exec {
            commandLine = listOf("git", "rev-parse", "--short", "HEAD")
            standardOutput = outputStream
        }
        outputStream.toString().trim()
    }
}

fun getVersion(): String {
    val version = System.getenv("VERSION")
    return if (!version.isNullOrBlank()) {
        version
    } else {
        // WARNING - feels like too much magic I should probably just error out here too.
        println("[WARNING]VERSION environment variable is not set. Using commit hash and SNAPSHOT version.")
        "SNAPSHOT-${getCommitHash()}"
    }
}
