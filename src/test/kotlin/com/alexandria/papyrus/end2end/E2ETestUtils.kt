package com.alexandria.papyrus.end2end

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers
import java.io.File

fun createAFolderTemplate(folderTemplateName: String): String {
    val requestUrl = "v1/folder-templates"
    val requestBody = """ { "name": "$folderTemplateName" } """

    return given()
        .auth().basic("user", "user")
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
        .auth().basic("user", "user")
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
        .auth().basic("user", "user")
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

fun renameFolderTemplate(
    folderTemplateId: String,
    newName: String,
) {
    val requestUrl = "v1/folder-templates/$folderTemplateId/rename"
    val requestBody = """ { "name": "$newName" } """

    given()
        .auth().basic("user", "user")
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}

fun createDocument(
    name: String,
    folderId: String,
    file: File,
): String {
    val requestUrl = "v1/documents"
    val requestBody = """ { "name": "$name", "folderIdentifier": "$folderId" } """

    return given()
        .auth().basic("user", "user")
        .contentType("multipart/form-data")
        .multiPart("createDocumentRequest", requestBody, "application/json")
        .multiPart("file", file)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(201)
        .header("Location", Matchers.notNullValue())
        .extract()
        .header("Location")
}

fun renameDocument(
    documentId: String,
    name: String,
) {
    val requestUrl = "v1/documents/$documentId/rename"
    val requestBody = """ { "name": "$name" } """

    given()
        .auth().basic("user", "user")
        .contentType("application/json")
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}

fun changeDocumentStatus(
    documentId: String,
    status: String,
) {
    val requestUrl = "v1/documents/$documentId/change-status"
    val requestBody = """ { "status": "$status" } """

    given()
        .auth().basic("user", "user")
        .contentType("application/json")
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}

fun createDocumentCategory(name: String): String {
    val requestUrl = "v1/document-categories"
    val requestBody = """ { "name": "$name" } """

    return given()
        .auth().basic("user", "user")
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

fun changeDocumentCategory(
    documentId: String,
    documentCategoryId: String,
) {
    val requestUrl = "v1/documents/$documentId/change-category"
    val requestBody = """ { "categoryIdentifier": "$documentCategoryId" } """

    given()
        .auth().basic("user", "user")
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}

fun changePredictedDocumentCategory(
    documentId: String,
    documentCategoryId: String,
) {
    val requestUrl = "v1/documents/$documentId/change-predicted-category"
    val requestBody = """ { "categoryIdentifier": "$documentCategoryId" } """

    given()
        .auth().basic("user", "user")
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}

fun changeFolderTemplateAssociatedDocumentCategory(
    folderTemplateIdentifier: String,
    documentCategoryId: String,
) {
    val requestUrl = "v1/folder-templates/$folderTemplateIdentifier/change-associated-category"
    val requestBody = """ { "categoryIdentifier": "$documentCategoryId" } """

    given()
        .auth().basic("user", "user")
        .contentType(ContentType.JSON)
        .body(requestBody)
        .post(requestUrl)
        .then()
        .assertThat()
        .statusCode(204)
}
