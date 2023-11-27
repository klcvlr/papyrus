package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.repositories.FolderRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class FolderJpaRepository(
    private val folderDAO: FolderDAO,
) : FolderRepository {
    override fun findByIdentifier(identifier: String): Folder? = folderDAO.findById(identifier).getOrNull()

    override fun findAll(): List<Folder> = folderDAO.findAll().toList()

    override fun save(folder: Folder) {
        folderDAO.save(folder)
    }

    override fun saveAll(folders: Collection<Folder>) {
        folderDAO.saveAll(folders)
    }

    override fun findAllByTemplate(templateIdentifier: String): List<Folder> = folderDAO.findAllByTemplate(templateIdentifier)
}

interface FolderDAO : CrudRepository<Folder, String> {
    @Query("SELECT f FROM Folder f WHERE f._template._identifier = :identifier")
    fun findAllByTemplate(identifier: String): List<Folder>
}
