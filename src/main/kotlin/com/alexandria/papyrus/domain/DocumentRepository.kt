package com.alexandria.papyrus.domain

interface DocumentRepository {
    fun findByIdentifier(documentIdentifier: String): Document
    fun save(document: Document)
}