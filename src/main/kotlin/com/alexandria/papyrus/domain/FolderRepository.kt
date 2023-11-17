package com.alexandria.papyrus.domain

interface FolderRepository {
    fun save(folder: Folder)
    fun saveAll(folders: Collection<Folder>)
    fun findByIdentifier(identifier: String): Folder?
    fun findAll(): List<Folder>
}