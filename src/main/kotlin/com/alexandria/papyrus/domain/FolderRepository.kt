package com.alexandria.papyrus.domain

interface FolderRepository {
    fun findByIdentifier(identifier: String): Folder
    fun findAll(): List<Folder>
    fun save(folder: Folder)
    fun saveAll(folders: Collection<Folder>)
}