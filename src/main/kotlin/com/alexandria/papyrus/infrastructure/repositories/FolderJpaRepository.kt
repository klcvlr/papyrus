package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.domain.model.Folder
import com.alexandria.papyrus.domain.repositories.FolderRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
class FolderJpaRepository(
    private val folderDAO: FolderDAO
) : FolderRepository {
    override fun findByIdentifier(identifier: String): Folder {
        return folderDAO.findById(identifier).get()
    }

    override fun findAll(): List<Folder> {
        return folderDAO.findAll().toList()
    }

    override fun save(folder: Folder) {
        folderDAO.save(folder)
    }

    override fun saveAll(folders: Collection<Folder>) {
        folderDAO.saveAll(folders)
    }
}

interface FolderDAO : CrudRepository<Folder, String>
