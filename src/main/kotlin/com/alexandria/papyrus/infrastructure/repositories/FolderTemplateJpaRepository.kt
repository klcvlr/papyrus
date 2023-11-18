package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.FolderTemplate
import com.alexandria.papyrus.domain.FolderTemplateRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
class FolderTemplateJpaRepository(
    private val folderTemplateDAO: FolderTemplateDAO
): FolderTemplateRepository {
    override fun findByIdentifier(identifier: String): FolderTemplate {
        return folderTemplateDAO.findById(identifier).get()
    }

    override fun save(folderTemplate: FolderTemplate) {
        folderTemplateDAO.save(folderTemplate)
    }
}

interface FolderTemplateDAO : CrudRepository<FolderTemplate, String>
