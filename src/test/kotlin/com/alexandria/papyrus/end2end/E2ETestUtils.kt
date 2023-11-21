package com.alexandria.papyrus.end2end

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers

fun createAFolderTemplate(folderTemplateName: String): String {
    val requestUrl = "v1/folder-templates"
    val requestBody = """ { "name": "$folderTemplateName" } """

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun createSubFolderTemplate(
    folderTemplateId: String,
    subFolderTemplateName: String,
): String {
    val requestUrl = "v1/folder-templates/$folderTemplateId/create-sub-folder"
    val requestBody = """ { "name": "$subFolderTemplateName" } """

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun createFolderFromTemplate(folderTemplateId: String): String {
    val requestUrl = "v1/folders"
    val requestBody = """ { "templateIdentifier": "$folderTemplateId" } """

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun createDocument(
    name: String,
    folderId: String,
): String {
    val requestUrl = "v1/documents"
    val requestBody = """ { "name": "$name", "folderIdentifier": "$folderId" } """

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun createDocumentType(name: String): String {
    val requestUrl = "v1/document-types"
    val requestBody = """ { "name": "$name" } """

    return given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun changeDocumentType(
    documentId: String,
    documentTypeId: String,
) {
    val requestUrl = "v1/documents/$documentId/change-type"
    val requestBody = """ { "typeIdentifier": "$documentTypeId" } """

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(200)
}

fun changePredictedDocumentType(
    documentId: String,
    documentTypeId: String,
) {
    val requestUrl = "v1/documents/$documentId/change-predicted-type"
    val requestBody = """ { "typeIdentifier": "$documentTypeId" } """

    given()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(200)
}
