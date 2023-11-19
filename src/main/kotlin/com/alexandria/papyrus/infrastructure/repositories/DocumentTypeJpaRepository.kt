package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
class DocumentTypeJpaRepository(
    private val documentTypeDAO: DocumentTypeDAO
) : DocumentTypeRepository {
    override fun findByIdentifier(identifier: String): DocumentType {
        return documentTypeDAO.findById(identifier).get()
    }

    override fun save(documentType: DocumentType) {
        documentTypeDAO.save(documentType)
    }
}

interface DocumentTypeDAO : CrudRepository<DocumentType, String>
