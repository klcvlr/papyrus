package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.DocumentType

interface DocumentTypeRepository {
    fun findByIdentifier(identifier: String): DocumentType
    fun save(documentType: DocumentType)
}
