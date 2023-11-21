package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentTypeNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.DocumentType
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
class DocumentTypeUseCases(
    private val idGenerator: IdGenerator,
    private val documentTypeRepository: DocumentTypeRepository,
) {
    @Transactional(readOnly = true)
    fun findByIdentifier(identifier: String): DocumentType {
        return documentTypeRepository.findByIdentifier(identifier) ?: throw DocumentTypeNotFoundException(identifier)
    }

    fun create(name: String): String {
        val documentType =
            DocumentType(
                identifier = idGenerator.generate(),
                name = name,
            )
        documentTypeRepository.save(documentType)
        return documentType.identifier
    }
}
