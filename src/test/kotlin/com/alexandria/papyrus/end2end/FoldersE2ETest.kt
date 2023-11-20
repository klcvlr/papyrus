package com.alexandria.papyrus.end2end

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FoldersE2ETest {
    @LocalServerPort
    private var port: Int = 0


    @Test
    fun `create folder from a folder template`() {
        val createFolderTemplateUrl = "http://localhost:$port/api/v1/folder-templates"
        val createFolderUrl = "http://localhost:$port/api/v1/folders"

        // CREATE ROOT FOLDER TEMPLATE
        val createRootFolderTemplateRequestBody = """ { "folderTemplateName": "rootFolder" } """
        val rootFolderTemplateLocation =
            given()
                .contentType(ContentType.JSON)
                .body(createRootFolderTemplateRequestBody)
                .post(createFolderTemplateUrl)
                .then()
                .assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")
        val rootFolderTemplateId = rootFolderTemplateLocation.split("/").last()

        // CREATE SUB FOLDER TEMPLATE
        val addSubFolderTemplateRequestBody = """ { "folderTemplateName": "subFolder" } """
        val addSubFolderTemplateUrl =
            "http://localhost:$port/api/v1/folder-templates/$rootFolderTemplateId/add-sub-folder-template"
        given()
            .contentType(ContentType.JSON)
            .body(addSubFolderTemplateRequestBody)
            .post(addSubFolderTemplateUrl)
            .then()
            .assertThat()
            .statusCode(201)
            .header("Location", notNullValue())

        // CREATE FOLDER FROM TEMPLATE
        val createFolderRequestBody = """ { "folderTemplateIdentifier": "$rootFolderTemplateId" } """
        val folderLocation =
            given()
                .contentType(ContentType.JSON)
                .body(createFolderRequestBody)
                .post(createFolderUrl)
                .then()
                .assertThat()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract()
                .header("Location")

        // GET CREATED FOLDER AND ASSERT IT WAS CREATED LIKE THE TEMPLATE
        given()
            .get(folderLocation)
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("identifier", notNullValue())
            .body("name", equalTo("rootFolderTemplate"))
            .body("associatedType", nullValue())
            .body("parentFolderIdentifier", nullValue())
            .body("subFolders", nullValue())
    }
}
