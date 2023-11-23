package com.alexandria.papyrus.adapters.rest

import com.alexandria.papyrus.adapters.rest.DocumentTypeView.Companion.toDocumentTypeView
import com.alexandria.papyrus.domain.model.Document

data class CreateDocumentRequest(val name: String, val folderIdentifier: String)

data class ChangeTypeRequest(val typeIdentifier: String)

data class ChangePredictedTypeRequest(val typeIdentifier: String)

data class DocumentView(
    val identifier: String,
    val name: String,
    val parentFolderIdentifier: String,
    val type: DocumentTypeView?,
    val predictedType: DocumentTypeView?,
) {
    companion object {
        fun toDocumentView(document: Document): DocumentView {
            return DocumentView(
                identifier = document.identifier,
                name = document.name,
                parentFolderIdentifier = document.parentFolder.identifier,
                type = if (document.type != null) toDocumentTypeView(document.type!!) else null,
                predictedType = if (document.predictedType != null) toDocumentTypeView(document.predictedType!!) else null,
            )
        }
    }
}
