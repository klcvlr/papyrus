package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.domain.model.DocumentCategory

data class CreateDocumentCategoryRequest(val name: String)

data class DocumentCategoryView(val identifier: String, val name: String, val user: String) {
    companion object {
        fun toDocumentCategoryView(documentCategory: DocumentCategory): DocumentCategoryView =
            DocumentCategoryView(identifier = documentCategory.identifier, name = documentCategory.name, user = documentCategory.user)
    }
}
