package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
class DocumentJpaRepository(
    private val documentDAO: DocumentDAO
) : DocumentRepository {
    override fun findByIdentifier(documentIdentifier: String): Document {
        return documentDAO.findById(documentIdentifier).get()
    }

    override fun findAll(): List<Document> {
        return documentDAO.findAll().toList()
    }

    override fun save(document: Document) {
        documentDAO.save(document)
    }
}

interface DocumentDAO : CrudRepository<Document, String>
