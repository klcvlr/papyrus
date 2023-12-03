package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.DocumentCategory

interface DocumentCategoryRepository {
    fun findByIdentifier(identifier: String): DocumentCategory?

    fun save(documentCategory: DocumentCategory): DocumentCategory
}
