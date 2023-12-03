package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.model.DocumentCategory
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class DocumentCategoryJpaRepository(
    private val documentCategoryDAO: DocumentCategoryDAO,
) : DocumentCategoryRepository {
    override fun findByIdentifier(identifier: String): DocumentCategory? = documentCategoryDAO.findById(identifier).getOrNull()

    override fun save(documentCategory: DocumentCategory): DocumentCategory = documentCategoryDAO.save(documentCategory)
}

interface DocumentCategoryDAO : CrudRepository<DocumentCategory, String>
