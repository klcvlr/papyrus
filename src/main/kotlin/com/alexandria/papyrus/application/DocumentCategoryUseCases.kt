package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentCategoryNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.model.DocumentCategory
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
class DocumentCategoryUseCases(
    private val idGenerator: IdGenerator,
    private val documentCategoryRepository: DocumentCategoryRepository,
) {
    @Transactional(readOnly = true)
    fun findByIdentifier(identifier: String): DocumentCategory =
        documentCategoryRepository.findByIdentifier(identifier) ?: throw DocumentCategoryNotFoundException(identifier)

    fun create(
        name: String,
        user: String,
    ): String {
        val documentCategory = DocumentCategory(identifier = idGenerator.generate(), name = name, user = user)
        documentCategoryRepository.save(documentCategory)
        return documentCategory.identifier
    }
}
