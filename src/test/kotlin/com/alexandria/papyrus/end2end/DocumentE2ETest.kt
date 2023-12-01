package com.alexandria.papyrus.end2end

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.ResourceLoader
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

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/api/"
    }

    @Test
    fun `create a document`() {
        val folderTemplateLocation = createAFolderTemplate("newFolderTemplate")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        val folderLocation = createFolderFromTemplate(folderTemplateId)
        val folderId = folderLocation.split("/").last()

        val file = resourceLoader.getResource("classpath:banner.txt").file
        val documentLocation = createDocument("newDocument", folderId, file)

        given()
            .auth().basic("user", "user")
            .get(documentLocation)
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", notNullValue())
            .body("name", equalTo("newDocument"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", equalTo(folderId))
            .body("user", equalTo("user"))
            .body("status", equalTo("CREATED"))
            .body("fileIdentifier", notNullValue())
    }

    @Test
    fun `uploaded files can be downloaded`() {
        val folderTemplateLocation = createAFolderTemplate("newFolderTemplate")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        val folderLocation = createFolderFromTemplate(folderTemplateId)
        val folderId = folderLocation.split("/").last()

        val textFile = resourceLoader.getResource("classpath:a_text_file.txt").file
        val textDocumentLocation = createDocument("a_text_file.txt", folderId, textFile)

        val pdfFile = resourceLoader.getResource("classpath:a_pdf_file.pdf").file
        val pdfDocumentLocation = createDocument("a_pdf_file.pdf", folderId, pdfFile)

        val pngFile = resourceLoader.getResource("classpath:a_png_file.png").file
        val pngDocumentLocation = createDocument("a_png_file.png", folderId, pngFile)

        given()
            .auth().basic("user", "user")
            .get("$textDocumentLocation/file")
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.TEXT)
            .body(equalTo("hello, this is the text file content!"))

        given()
            .auth().basic("user", "user")
            .get("$pdfDocumentLocation/file")
            .then().assertThat()
            .statusCode(200)
            .contentType("application/pdf")

        given()
            .auth().basic("user", "user")
            .get("$pngDocumentLocation/file")
            .then().assertThat()
            .statusCode(200)
            .contentType("image/png")
    }

    @Test
    fun `change a document's type`() {
        val folderTemplateLocation = createAFolderTemplate("newFolderTemplate")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        val folderLocation = createFolderFromTemplate(folderTemplateId)
        val folderId = folderLocation.split("/").last()

        val documentTypeLocation = createDocumentType("newDocumentType")
        val documentTypeId = documentTypeLocation.split("/").last()

        val file = resourceLoader.getResource("classpath:banner.txt").file
        val documentLocation = createDocument("newDocument", folderId, file)
        val documentId = documentLocation.split("/").last()

        changeDocumentType(documentId, documentTypeId)

        given()
            .auth().basic("user", "user")
            .get(documentLocation)
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(documentId))
            .body("name", equalTo("newDocument"))
            .body("type.identifier", equalTo(documentTypeId))
            .body("type.name", equalTo("newDocumentType"))
            .body("predictedType", nullValue())
            .body("parentFolderIdentifier", equalTo(folderId))
            .body("user", equalTo("user"))
            .body("fileIdentifier", notNullValue())
    }

    @Test
    fun `change a document's predicted type`() {
        val folderTemplateLocation = createAFolderTemplate("newFolderTemplate")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        val folderLocation = createFolderFromTemplate(folderTemplateId)
        val folderId = folderLocation.split("/").last()

        val documentTypeLocation = createDocumentType("newDocumentType")
        val documentTypeId = documentTypeLocation.split("/").last()

        val file = resourceLoader.getResource("classpath:banner.txt").file
        val documentLocation = createDocument("newDocument", folderId, file)
        val documentId = documentLocation.split("/").last()

        changePredictedDocumentType(documentId, documentTypeId)

        given()
            .auth().basic("user", "user")
            .get(documentLocation)
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", equalTo(documentId))
            .body("name", equalTo("newDocument"))
            .body("type", nullValue())
            .body("predictedType.identifier", equalTo(documentTypeId))
            .body("predictedType.name", equalTo("newDocumentType"))
            .body("parentFolderIdentifier", equalTo(folderId))
            .body("user", equalTo("user"))
            .body("fileIdentifier", notNullValue())
    }

    @Test
    fun `change a document's status`() {
        val folderTemplateLocation = createAFolderTemplate("newFolderTemplate")
        val folderTemplateId = folderTemplateLocation.split("/").last()

        val folderLocation = createFolderFromTemplate(folderTemplateId)
        val folderId = folderLocation.split("/").last()

        val file = resourceLoader.getResource("classpath:banner.txt").file
        val documentLocation = createDocument("newDocument", folderId, file)
        val documentId = documentLocation.split("/").last()

        changeDocumentStatus(documentId, "COMPLETED")

        given()
            .auth().basic("user", "user")
            .get(documentLocation)
            .then().assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", notNullValue())
            .body("name", equalTo("newDocument"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", equalTo(folderId))
            .body("user", equalTo("user"))
            .body("status", equalTo("COMPLETED"))
            .body("fileIdentifier", notNullValue())
    }

    @Test
    fun `get a document that does not exist returns a 404`() {
        given()
            .auth().basic("user", "user")
            .get("v1/documents/123")
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
