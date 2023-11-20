package com.alexandria.papyrus.end2end

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FolderTemplatesE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @Test
    fun testCreateAndGetFolderTemplate() {
        // Create the folder template and extract the URL from the 'Location' header
        val createFolderTemplateUrl = "http://localhost:$port/api/v1/folder-templates"
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
