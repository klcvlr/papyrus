package com.alexandria.papyrus.end2end

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
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
class FoldersE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/api/"
    }

    @Test
    fun `create folder from a folder template`() {
        val rootFolderTemplateLocation = createAFolderTemplate("rootFolder")
        val rootFolderTemplateId = rootFolderTemplateLocation.split("/").last()

        createSubFolderTemplate(rootFolderTemplateId, "subFolder")

        val folderLocation = createFolderFromTemplate(rootFolderTemplateId)
        val folderId = folderLocation.split("/").last()

        given()
            .auth().basic("user", "user")
            .get(folderLocation)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(folderId))
            .body("name", equalTo("rootFolder"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", nullValue())
            .body("subFolders.size()", equalTo(1))
            .body("subFolders[0].name", equalTo("subFolder"))
            .body("subFolders[0].parentFolderIdentifier", equalTo(folderId))
            .body("subFolders[0].associatedDocumentCategory", nullValue())
            .body("subFolders[0].documents.size()", equalTo(0))
            .body("subFolders[0].subFolders.size()", equalTo(0))
    }

    @Test
    fun `create a folder from a template that does not exist returns a 404`() {
        given()
            .auth().basic("user", "user")
            .contentType(ContentType.JSON)
            .body(""" { "templateIdentifier": "123" } """)
            .post("v1/folders")
            .then()
            .assertThat()
            .statusCode(404)
    }

    @Test
    fun `get request on a folder that does not exist returns a 404`() {
        given()
            .auth().basic("user", "user")
            .get("v1/folder/123")
            .then()
            .assertThat()
            .statusCode(404)
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
