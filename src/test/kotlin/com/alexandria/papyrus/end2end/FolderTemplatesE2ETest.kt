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
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class FolderTemplatesE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port/api"
    }


    @Test
    fun `create a folder template`() {
        // CREATE ROOT FOLDER TEMPLATE
        val createFolderTemplateUrl = "/v1/folder-templates"
        val createFolderTemplateRequestBody = """ { "folderTemplateName": "newFolderTemplate" } """
        val locationUrl =
            given()
                .contentType(ContentType.JSON)
                .body(createFolderTemplateRequestBody)
                .post(createFolderTemplateUrl)
                .then()
                .assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")

        // request to the URL provided in the 'Location' header
        given()
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
    }
}
