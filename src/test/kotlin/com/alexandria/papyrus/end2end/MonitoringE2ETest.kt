package com.alexandria.papyrus.end2end

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class MonitoringE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/actuator/"
    }

    @Test
    fun `monitoring HEALTH endpoint is accessible without authentication`() {
        given()
            .get("health")
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", equalTo("UP"))
    }

    @Test
    fun `monitoring INFO is not accessible without authentication`() {
        given()
            .get("info")
            .then().assertThat()
            .statusCode(401)
    }

    @Test
    fun `monitoring INFO is not accessible to ROLE USER`() {
        given()
            .auth().basic("user", "user")
            .get("info")
            .then().assertThat()
            .statusCode(403)
    }

    @Test
    fun `monitoring INFO is accessible to ROLE ADMIN`() {
        given()
            .auth().basic("admin", "admin")
            .get("info")
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("build.name", equalTo("papyrus"))
            .body("build.artifact", equalTo("papyrus"))
            .body("build.version", notNullValue())
    }

    companion object {
        @Container
        @JvmStatic
        private val postgresqlContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16.1-alpine")
                .withDatabaseName("papyrus")
                .withUsername("toth")
                .withPassword("parchment")

        @DynamicPropertySource
        @JvmStatic
        fun postgresqlProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }
}
