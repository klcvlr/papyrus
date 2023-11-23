package com.alexandria.papyrus.adapters.repositories

import com.alexandria.papyrus.domain.model.FolderTemplate
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class FolderTemplateJpaRepository(
    private val folderTemplateDAO: FolderTemplateDAO,
) : FolderTemplateRepository {
    override fun findByIdentifier(identifier: String): FolderTemplate? {
        return folderTemplateDAO.findById(identifier).getOrNull()
    }

    override fun findAllRoots(): List<FolderTemplate> {
        return folderTemplateDAO.findAllRoots().toList()
    }

    override fun save(folderTemplate: FolderTemplate) {
        folderTemplateDAO.save(folderTemplate)
    }
}

interface FolderTemplateDAO : CrudRepository<FolderTemplate, String> {
    @Query("SELECT ft FROM FolderTemplate ft WHERE ft._parentFolder IS NULL")
    fun findAllRoots(): List<FolderTemplate>
}
