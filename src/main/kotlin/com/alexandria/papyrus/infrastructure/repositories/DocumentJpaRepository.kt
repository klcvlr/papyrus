package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.model.Document
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class DocumentJpaRepository(
    private val documentDAO: DocumentDAO,
) : DocumentRepository {
    override fun findByIdentifier(documentIdentifier: String): Document? {
        return documentDAO.findById(documentIdentifier).getOrNull()
    }

    override fun save(document: Document) {
        documentDAO.save(document)
    }
}

interface DocumentDAO : CrudRepository<Document, String>
