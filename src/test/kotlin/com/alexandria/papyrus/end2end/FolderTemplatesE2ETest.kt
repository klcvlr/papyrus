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
class FolderTemplatesE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/api/"
    }

    @Test
    fun `create a folder template`() {
        val locationUrl = createAFolderTemplate("newFolderTemplate")

        given()
            .auth().basic("user", "user")
            .get(locationUrl)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", notNullValue())
            .body("name", equalTo("newFolderTemplate"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", nullValue())
            .body("subFolderTemplate", nullValue())
            .body("user", equalTo("user"))
    }

    @Test
    fun `a 404 status is return on a GET request for a folder template that does not exist`() {
        val nonExistingFolderTemplate = "123"
        given()
            .auth().basic("user", "user")
            .get("v1/folder-templates/$nonExistingFolderTemplate")
            .then()
            .assertThat()
            .statusCode(404)
    }

    @Test
    fun `a folder template can be renamed`() {
        val folderTemplateLocationUrl = createAFolderTemplate("folderTemplate")
        val folderTemplateId = folderTemplateLocationUrl.split("/").last()

        renameFolderTemplate(folderTemplateId, "newFolderTemplateName")

        given()
            .auth().basic("user", "user")
            .get(folderTemplateLocationUrl)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(folderTemplateId))
            .body("name", equalTo("newFolderTemplateName"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", nullValue())
            .body("subFolderTemplate", nullValue())
    }

    @Test
    fun `a folder template's associated type can be changed`() {
        val folderTemplateLocationUrl = createAFolderTemplate("folderTemplate")
        val folderTemplateId = folderTemplateLocationUrl.split("/").last()

        renameFolderTemplate(folderTemplateId, "newFolderTemplateName")

        given()
            .auth().basic("user", "user")
            .get(folderTemplateLocationUrl)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(folderTemplateId))
            .body("name", equalTo("newFolderTemplateName"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", nullValue())
            .body("subFolderTemplate", nullValue())
    }

    @Test
    fun `a folder template's associated document type can be changed`() {
        val folderTemplateLocationUrl = createAFolderTemplate("folderTemplateName")
        val folderTemplateId = folderTemplateLocationUrl.split("/").last()

        val createDocumentTypeLocation = createDocumentType("documentTypeName")
        val documentTypeId = createDocumentTypeLocation.split("/").last()

        changeFolderTemplateAssociatedDocumentType(folderTemplateId, documentTypeId)

        given()
            .auth().basic("user", "user")
            .get(folderTemplateLocationUrl)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(folderTemplateId))
            .body("name", equalTo("folderTemplateName"))
            .body("associatedDocumentType.identifier", equalTo(documentTypeId))
            .body("associatedDocumentType.name", equalTo("documentTypeName"))
            .body("parentFolderIdentifier", nullValue())
            .body("subFolderTemplate", nullValue())
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
