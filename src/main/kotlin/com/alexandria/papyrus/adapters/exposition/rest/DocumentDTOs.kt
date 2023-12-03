package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentCategoryView.Companion.toDocumentCategoryView
import com.alexandria.papyrus.domain.model.Document

data class CreateDocumentRequest(val name: String, val folderIdentifier: String)

data class RenameDocumentRequest(val name: String)

data class ChangeCategoryRequest(val categoryIdentifier: String)

data class ChangePredictedCategoryRequest(val categoryIdentifier: String)

data class ChangeStatusRequest(val status: String)

data class DocumentView(
    val identifier: String,
    val name: String,
    val parentFolderIdentifier: String,
    val category: DocumentCategoryView?,
    val predictedCategory: DocumentCategoryView?,
    val user: String,
    val status: String,
    val fileIdentifier: String,
) {
    companion object {
        fun toDocumentView(document: Document): DocumentView =
            DocumentView(
                identifier = document.identifier,
                name = document.name,
                parentFolderIdentifier = document.parentFolder.identifier,
                category = document.category?.let { toDocumentCategoryView(it) },
                predictedCategory = document.predictedCategory?.let { toDocumentCategoryView(it) },
                user = document.user,
                status = document.status,
                fileIdentifier = document.fileIdentifier,
            )
    }
}
