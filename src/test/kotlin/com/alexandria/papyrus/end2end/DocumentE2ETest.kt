package com.alexandria.papyrus.end2end

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
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
class DocumentE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/api/"
    }

    @Test
    fun `create a document`() {
        // CREATE ROOT FOLDER TEMPLATE
        val createFolderTemplateUrl = "v1/folder-templates"
        val createFolderTemplateRequestBody = """ { "name": "newFolderTemplate" } """

        val folderTemplateLocation =
            given()
                .contentType(ContentType.JSON)
                .body(createFolderTemplateRequestBody)
                .post(createFolderTemplateUrl)
                .then().assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        // CREATE FOLDER FROM TEMPLATE
        val createFolderFromTemplateUrl = "v1/folders"
        val createFolderFromTemplateRequestBody = """ { "templateIdentifier": "$folderTemplateId" } """

        val folderLocation =
            given()
                .contentType(ContentType.JSON)
                .body(createFolderFromTemplateRequestBody)
                .post(createFolderFromTemplateUrl)
                .then().assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")
        val folderId = folderLocation.split("/").last()

        // CREATE DOCUMENT
        val createDocumentUrl = "v1/documents"
        val createDocumentRequestBody = """ { "name": "newDocument", "folderIdentifier": "$folderId", "rootFolderIdentifier": "$folderId" } """

        val documentLocation =
            given()
                .contentType(ContentType.JSON)
                .body(createDocumentRequestBody)
                .post(createDocumentUrl)
                .then().assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")

        // GET DOCUMENT AND ASSERT IT HAS BEEN PROPERLY CREATED
        given()
            .get(documentLocation)
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", notNullValue())
            .body("name", equalTo("newDocument"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", equalTo(folderId))
            .body("rootFolderIdentifier", equalTo(folderId))
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
