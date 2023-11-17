package com.alexandria.papyrus.domain

interface DocumentTypeRepository {
    fun findByIdentifier(identifier: String): DocumentType
    fun save(documentType: DocumentType)
}