package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.domain.model.DocumentType

data class CreateDocumentTypeRequest(val name: String)

data class DocumentTypeView(
    val identifier: String,
    val name: String,
) {
    companion object {
        fun toDocumentTypeView(documentType: DocumentType): DocumentTypeView {
            return DocumentTypeView(
                identifier = documentType.identifier,
                name = documentType.name,
            )
        }
    }
}
