package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.model.FolderTemplate
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
class FolderTemplateJpaRepository(
    private val folderTemplateDAO: FolderTemplateDAO,
) : FolderTemplateRepository {
    override fun findByIdentifier(identifier: String): FolderTemplate {
        return folderTemplateDAO.findById(identifier).get()
    }

    override fun findAll(): List<FolderTemplate> {
        return folderTemplateDAO.findAll().toList()
    }

    override fun save(folderTemplate: FolderTemplate) {
        folderTemplateDAO.save(folderTemplate)
    }

    override fun saveAll(folderTemplates: List<FolderTemplate>) {
        folderTemplateDAO.saveAll(folderTemplates)
    }
}

interface FolderTemplateDAO : CrudRepository<FolderTemplate, String>
