package com.alexandria.papyrus.infrastructure.api

import com.alexandria.papyrus.domain.model.Document

data class CreateDocumentRequest(val documentName: String, val parentFolderIdentifier: String)

data class DocumentView(
    val identifier: String,
    val name: String,
    val parentFolderIdentifier: String,
    val type: String?,
    val predictedType: String?,
) {
    companion object {
        fun toDocumentView(document: Document): DocumentView {
            return DocumentView(
                identifier = document.identifier,
                name = document.name,
                parentFolderIdentifier = document.parentFolder.identifier,
                type = document.type?.identifier,
                predictedType = document.predictedType?.identifier,
            )
        }
    }
}
