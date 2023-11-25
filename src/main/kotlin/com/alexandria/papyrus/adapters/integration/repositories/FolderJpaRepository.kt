package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.repositories.FolderRepository
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
}

interface FolderDAO : CrudRepository<Folder, String>
