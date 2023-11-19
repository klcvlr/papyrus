package com.alexandria.papyrus.domain.repositories

import com.alexandria.papyrus.domain.model.Folder

interface FolderRepository {
    fun findByIdentifier(identifier: String): Folder?

    fun findAll(): List<Folder>

    fun save(folder: Folder)

    fun saveAll(folders: Collection<Folder>)
}
