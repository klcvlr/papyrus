package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.Document

interface DocumentRepository {
    fun findByIdentifier(documentIdentifier: String): Document?

    fun save(document: Document)
}
